package com.homemenuplanner.services;

import com.homemenuplanner.dtos.ingredient.RecipeIngredientRequest;
import com.homemenuplanner.dtos.recipe.RecipeRequest;
import com.homemenuplanner.dtos.recipe.RecipeResponse;
import com.homemenuplanner.mappers.RecipeMapper;
import com.homemenuplanner.models.Ingredient;
import com.homemenuplanner.models.Recipe;
import com.homemenuplanner.models.RecipeIngredient;
import com.homemenuplanner.models.RecipeIngredientId;
import com.homemenuplanner.repositories.RecipeRepository;
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

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    // Converts Recipe entity to RecipeResponse
    private RecipeResponse mapToResponse(Recipe recipe) {
        return new RecipeResponse(recipe.getId(), recipe.getName(), recipe.getInstructions(), null);
    }


    public RecipeResponse addRecipe(RecipeRequest recipeRequest) {
        Recipe recipe = new Recipe();
        recipe.setName(recipeRequest.getName());
        recipe.setInstructions(recipeRequest.getInstructions());
        recipe.setDescription(recipeRequest.getDescription());
        recipe.setCookbookName(recipeRequest.getCookbook());
        recipe.setPage(recipeRequest.getPage());
        recipe.setUrl(recipeRequest.getUrl());
        recipe.setImageFileName(recipeRequest.getImageFileName());

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
}
