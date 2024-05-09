package com.homemenuplanner.dtos.recipe;

import com.homemenuplanner.dtos.ingredient.IngredientResponse;
import com.homemenuplanner.dtos.ingredient.RecipeIngredientResponse;
import com.homemenuplanner.models.Recipe;
import com.homemenuplanner.models.RecipeIngredient;

import java.util.List;

public class RecipeResponse {
    private Long id;
    private String name;
    private String instructions;
    private List<RecipeIngredientResponse> ingredients;

    public RecipeResponse() {
    }

    public RecipeResponse(Long id, String name, String instructions, List<RecipeIngredientResponse> ingredients) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.ingredients = ingredients;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<RecipeIngredientResponse> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredientResponse> ingredients) {
        this.ingredients = ingredients;
    }
}

