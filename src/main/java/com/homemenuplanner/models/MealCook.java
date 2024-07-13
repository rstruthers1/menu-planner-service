package com.homemenuplanner.models;

import jakarta.persistence.*;

@Entity
@Table(name = "meal_cooks")
public class MealCook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_planning_day_meal_id", columnDefinition = "int UNSIGNED not null")
    private Long mealPlanningDayMealId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", columnDefinition = "int UNSIGNED not null")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "meal_planning_day_meal_id", nullable = false, insertable = false, updatable = false)
    private MealPlanningDayMeal mealPlanningDayMeal;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;

    // Getters and Setters

    public Long getMealPlanningDayMealId() {
        return mealPlanningDayMealId;
    }

    public void setMealPlanningDayMealId(Long mealPlanningDayMealId) {
        this.mealPlanningDayMealId = mealPlanningDayMealId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public MealPlanningDayMeal getMealPlanningDayMeal() {
        return mealPlanningDayMeal;
    }

    public void setMealPlanningDayMeal(MealPlanningDayMeal mealPlanningDayMeal) {
        this.mealPlanningDayMeal = mealPlanningDayMeal;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

