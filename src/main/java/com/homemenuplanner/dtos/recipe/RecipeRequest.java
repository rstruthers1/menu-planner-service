package com.homemenuplanner.dtos.recipe;

import com.homemenuplanner.dtos.ingredient.RecipeIngredientRequest;

import java.util.List;

public class RecipeRequest {
    private String name;
    private String instructions;
    private String description;
    private String url;
    private String cookbook;
    private Integer page;
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


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCookbook() {
        return cookbook;
    }

    public void setCookbook(String cookbook) {
        this.cookbook = cookbook;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
