package com.homemenuplanner.dtos.mealplanningweek;


public class MealPlanningWeekRequest {
    private Long groupId;
    private String startDate;

    // Getters and Setters
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}

