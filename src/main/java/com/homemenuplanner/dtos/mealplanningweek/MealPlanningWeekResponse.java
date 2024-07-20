package com.homemenuplanner.dtos.mealplanningweek;

import com.homemenuplanner.dtos.mealplanningday.MealPlanningDayResponse;
import java.util.List;

public class MealPlanningWeekResponse {
    private List<MealPlanningDayResponse> mealPlanningDays;

    // Getters and Setters
    public List<MealPlanningDayResponse> getMealPlanningDays() {
        return mealPlanningDays;
    }

    public void setMealPlanningDays(List<MealPlanningDayResponse> mealPlanningDays) {
        this.mealPlanningDays = mealPlanningDays;
    }
}
