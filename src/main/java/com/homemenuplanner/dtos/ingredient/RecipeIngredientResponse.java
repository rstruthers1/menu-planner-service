package com.homemenuplanner.dtos.ingredient;

public class RecipeIngredientResponse {
    private Long recipeId;
    private Long ingredientId;
    private String name;
    private String unit;
    private Double quantity;

    public RecipeIngredientResponse() {
    }

    public RecipeIngredientResponse(Long recipeId, Long ingredientId, String name, String unit, Double quantity) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.name = name;
        this.unit = unit;
        this.quantity = quantity;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setRecipeId(Long recipeId) {

        this.recipeId = recipeId;
    }

    public void setIngredientId(Long ingredientId) {

        this.ingredientId = ingredientId;
    }




    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public Double getQuantity() {
        return quantity;
    }


    public void setName(String name) {
    }

    public void setAmount(double amount) {
    }

    public void setUnit(String unit) {
    }

    public void setNote(String note) {

    }
}
