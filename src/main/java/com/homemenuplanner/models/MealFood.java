package com.homemenuplanner.models;

import jakarta.persistence.*;

@Entity
@Table(name = "meal_foods")
public class MealFood {
    @EmbeddedId
    private MealFoodId id;

    @ManyToOne
    @MapsId("mealId")
    @JoinColumn(name = "meal_id")
    private Meal meal;

    @ManyToOne
    @MapsId("foodId")
    @JoinColumn(name = "food_id")
    private Food food;

    public MealFood() {}

    public MealFood(Meal meal, Food food) {
        this.id = new MealFoodId(meal.getId(), food.getId());
        this.meal = meal;
        this.food = food;
    }

    // Getters and Setters
    public MealFoodId getId() {
        return id;
    }

    public void setId(MealFoodId id) {
        this.id = id;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }
}
