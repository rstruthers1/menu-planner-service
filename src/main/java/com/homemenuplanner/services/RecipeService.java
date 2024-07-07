package com.homemenuplanner.services;

import com.homemenuplanner.dtos.ingredient.RecipeIngredientRequest;
import com.homemenuplanner.dtos.recipe.RecipeRequest;
import com.homemenuplanner.dtos.recipe.RecipeResponse;
import com.homemenuplanner.exceptions.ResourceNotFoundException;
import com.homemenuplanner.mappers.RecipeMapper;
import com.homemenuplanner.models.*;
import com.homemenuplanner.repositories.CookbookRepository;
import com.homemenuplanner.repositories.RecipeRepository;
import com.homemenuplanner.repositories.UserGroupRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final CookbookRepository cookbookRepository;
    private final UserGroupRepository groupRepository;

    public RecipeService(RecipeRepository recipeRepository, CookbookRepository cookbookRepository,
                         UserGroupRepository groupRepository) {
        this.recipeRepository = recipeRepository;
        this.cookbookRepository = cookbookRepository;
        this.groupRepository = groupRepository;
    }

    // Converts Recipe entity to RecipeResponse
    private RecipeResponse mapToResponse(Recipe recipe) {
        return new RecipeResponse(recipe.getId(), recipe.getName(), recipe.getInstructions(), null);
    }


    public RecipeResponse addRecipe(RecipeRequest recipeRequest) {
        Recipe recipe = new Recipe();
        return saveRecipeAndMapResponse(recipeRequest, recipe);
    }

    public RecipeResponse updateRecipe(Long id, RecipeRequest recipeRequest) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id " + id));
        return saveRecipeAndMapResponse(recipeRequest, recipe);
    }

    private RecipeResponse saveRecipeAndMapResponse(RecipeRequest recipeRequest, Recipe recipe) {
        recipe.setName(recipeRequest.getName());
        recipe.setInstructions(recipeRequest.getInstructions());
        recipe.setDescription(recipeRequest.getDescription());
        recipe.setPage(recipeRequest.getPage());
        recipe.setUrl(recipeRequest.getUrl());
        recipe.setImageFileName(recipeRequest.getImageFileName());
        recipe.setIsPublic(recipeRequest.getIsPublic());

        // Set the group if the groupId is not null
        if (recipeRequest.getGroupId() != null) {
            UserGroup userGroup = groupRepository.findById(recipeRequest.getGroupId())
                    .orElseThrow(() -> new ResourceNotFoundException("Group not found with id " + recipeRequest.getGroupId()));
            recipe.setUserGroup(userGroup);
        }

        // Set the cookbook if the cookbookId is not null
        if (recipeRequest.getCookbookId() != null) {
            Cookbook cookbook = cookbookRepository.findById(recipeRequest.getCookbookId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cookbook not found with id " + recipeRequest.getCookbookId()));
            recipe.setCookbook(cookbook);
        }

        Recipe savedRecipe = recipeRepository.save(recipe);
        if (recipeRequest.getIngredients() != null) {
            Set<RecipeIngredient> recipeIngredients = new HashSet<>();
            for (RecipeIngredientRequest recipeIngredientRequest : recipeRequest.getIngredients()) {
                RecipeIngredient recipeIngredient = getRecipeIngredient(recipeIngredientRequest, savedRecipe);
                recipeIngredients.add(recipeIngredient);
            }
            savedRecipe.setRecipeIngredients(recipeIngredients);

            recipeRepository.save(savedRecipe);
        }

        return RecipeMapper.toRecipeResponse(savedRecipe);
    }

    private static RecipeIngredient getRecipeIngredient(RecipeIngredientRequest recipeIngredientRequest, Recipe savedRecipe) {
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        Ingredient ingredient = new Ingredient();
        ingredient.setId(recipeIngredientRequest.getIngredientId());
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setRecipe(savedRecipe);
        RecipeIngredientId recipeIngredientId = new RecipeIngredientId();
        recipeIngredientId.setIngredientId(ingredient.getId());
        recipeIngredientId.setRecipeId(savedRecipe.getId());
        recipeIngredient.setId(recipeIngredientId);
        return recipeIngredient;
    }

    // TODO: remove this and change to a paginated response with a limit
    public List<RecipeResponse> getAllRecipes() {
        return recipeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Page<Recipe> searchRecipesByName(String name, Pageable pageable) {
        return recipeRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public Page<Recipe> searchRecipesByNameAndGroupOrPublic(String name, Long groupId, Pageable pageable) {
        return recipeRepository.findByNameContainingIgnoreCaseAndUserGroupOrIsPublic(name, groupId, pageable);
    }

    public Page<Recipe> searchRecipesByNameAndGroup(String name, Long groupId, Pageable pageable) {
        return recipeRepository.findByNameContainingIgnoreCaseAndUserGroup(name, groupId, pageable);
    }

    public Page<Recipe> searchPublicRecipesByName(String name, Pageable pageable) {
        return recipeRepository.findByNameContainingIgnoreCaseAndIsPublic(name, pageable);
    }

}
