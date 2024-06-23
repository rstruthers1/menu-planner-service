package com.homemenuplanner.mappers;



import com.homemenuplanner.dtos.cookbook.CookbookResponse;
import com.homemenuplanner.dtos.ingredient.RecipeIngredientRequest;
import com.homemenuplanner.dtos.ingredient.RecipeIngredientResponse;
import com.homemenuplanner.dtos.recipe.RecipeRequest;
import com.homemenuplanner.models.Ingredient;
import com.homemenuplanner.models.Recipe;
import com.homemenuplanner.models.RecipeIngredient;
import com.homemenuplanner.dtos.recipe.RecipeResponse;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RecipeMapper {

    public static Recipe toRecipe(RecipeRequest request) {
        Recipe recipe = new Recipe();
        recipe.setName(request.getName());
        recipe.setInstructions(request.getInstructions());

        Set<RecipeIngredient> recipeIngredients = request.getIngredients().stream()
                .map(RecipeMapper::toRecipeIngredient)
                .collect(Collectors.toSet());
        recipe.setRecipeIngredients(recipeIngredients);
        return recipe;
    }

    public static RecipeIngredient toRecipeIngredient(RecipeIngredientRequest request) {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(request.getIngredientId());

        RecipeIngredient recipeIngredient =  new RecipeIngredient();
        recipeIngredient.setIngredient(ingredient);
        return recipeIngredient;
    }

    public static RecipeResponse toRecipeResponse(Recipe recipe) {
        RecipeResponse response = new RecipeResponse();
        response.setId(recipe.getId());
        response.setName(recipe.getName());
        response.setInstructions(recipe.getInstructions());
        response.setDescription(recipe.getDescription());
        if (recipe.getCookbook() != null) {
            CookbookResponse cookbookResponse = new CookbookResponse();
            cookbookResponse.setId(recipe.getCookbook().getId());
            cookbookResponse.setName(recipe.getCookbook().getName());
            cookbookResponse.setImageFileName(recipe.getCookbook().getImageFileName());
        }
        Set<RecipeIngredient> recipeIngredients = recipe.getRecipeIngredients();
        if (recipeIngredients == null) {
            return response;
        }
        List<RecipeIngredientResponse> ingredients = recipeIngredients.stream()
                .map(RecipeMapper::toRecipeIngredientResponse)
                .collect(Collectors.toList());
        response.setIngredients(ingredients);
        return response;
    }

    public static RecipeIngredientResponse toRecipeIngredientResponse(RecipeIngredient recipeIngredient) {
        RecipeIngredientResponse response = new RecipeIngredientResponse();
        response.setRecipeId(recipeIngredient.getRecipe().getId());
        response.setIngredientId(recipeIngredient.getIngredient().getId());
        response.setName(recipeIngredient.getIngredient().getName());
        response.setAmount(recipeIngredient.getAmount());
        response.setNote(recipeIngredient.getNotes());
        return response;
    }
}

