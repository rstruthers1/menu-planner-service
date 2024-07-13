package com.homemenuplanner.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MealFoodId implements Serializable {
    private Long mealId;
    private Long foodId;

    public MealFoodId() {}

    public MealFoodId(Long mealId, Long foodId) {
        this.mealId = mealId;
        this.foodId = foodId;
    }

    // Getters, Setters, equals(), and hashCode() methods
    public Long getMealId() {
        return mealId;
    }

    public void setMealId(Long mealId) {
        this.mealId = mealId;
    }

    public Long getFoodId() {
        return foodId;
    }

    public void setFoodId(Long foodId) {
        this.foodId = foodId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealFoodId that = (MealFoodId) o;
        return Objects.equals(mealId, that.mealId) && Objects.equals(foodId, that.foodId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mealId, foodId);
    }
}
