package com.homemenuplanner.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "meal_planning_day_meals")
public class MealPlanningDayMeal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int UNSIGNED not null")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "meal_planning_day_id", nullable = false)
    private MealPlanningDay mealPlanningDay;

    @ManyToOne
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;

    @Column(name = "meal_type", nullable = false)
    private String mealType;

    @OneToMany(mappedBy = "mealPlanningDayMeal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealCook> cooks;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MealPlanningDay getMealPlanningDay() {
        return mealPlanningDay;
    }

    public void setMealPlanningDay(MealPlanningDay mealPlanningDay) {
        this.mealPlanningDay = mealPlanningDay;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public List<MealCook> getCooks() {
        return cooks;
    }

    public void setCooks(List<MealCook> cooks) {
        this.cooks = cooks;
    }
}
