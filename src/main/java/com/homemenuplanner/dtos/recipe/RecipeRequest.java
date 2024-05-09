package com.homemenuplanner.dtos.recipe;

import com.homemenuplanner.dtos.ingredient.RecipeIngredientRequest;

import java.util.List;

public class RecipeRequest {
    private String name;
    private String instructions;
    private List<RecipeIngredientRequest> ingredients;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public List<RecipeIngredientRequest> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredientRequest> ingredients) {
        this.ingredients = ingredients;
    }
}
