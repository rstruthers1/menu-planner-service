package com.homemenuplanner.models;

import jakarta.persistence.*;

@Entity
@Table(name = "recipe_ingredients")
public class RecipeIngredient {
    @EmbeddedId
    private RecipeIngredientId id;

    @MapsId("recipeId")
    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @MapsId("ingredientId")
    @ManyToOne
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Column(nullable = false)
    private double amount;

//    @Column(name = "unit", nullable = true)
//    private String unit;

    @Column(columnDefinition = "TEXT")
    private String notes;

    // Constructors, getters, and setters
    public RecipeIngredient() {
    }

    public RecipeIngredient(Recipe recipe, Ingredient ingredient, double amount,  String note) {
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.amount = amount;
        this.notes = note;
    }



    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String note) {
        this.notes = note;
    }

    public RecipeIngredientId getId() {
        return id;
    }

    public void setId(RecipeIngredientId id) {
        this.id = id;
    }
}

