package com.homemenuplanner.dtos.recipe;

import com.homemenuplanner.dtos.ingredient.RecipeIngredientResponse;

import java.util.List;

public class RecipeResponse {
    private Long id;
    private String name;
    private String instructions;
    private String description;
    private String cookbook;
    private Integer page;
    private String url;
    private String imageFileName;

    private List<RecipeIngredientResponse> ingredients;

    public RecipeResponse() {
    }

    public RecipeResponse(Long id, String name, String instructions, String description, String cookbook, Integer page, String url, String imageFileName, List<RecipeIngredientResponse> ingredients) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.description = description;
        this.cookbook = cookbook;
        this.page = page;
        this.url = url;
        this.imageFileName = imageFileName;
        this.ingredients = ingredients;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }
}

