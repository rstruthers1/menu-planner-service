package com.homemenuplanner.dtos.mealplanningday;

import com.homemenuplanner.models.MealPlanningDay;

import java.math.BigDecimal;

public class MealPlanningDayResponse {
    private Long id;
    private Long groupId;
    private String date;
    private BigDecimal weatherHighTemp;
    private BigDecimal weatherLowTemp;
    private String weatherDescription;
    private String temperatureUnit;

    public MealPlanningDayResponse(MealPlanningDay mealPlanningDay) {
        this.id = mealPlanningDay.getId();
        this.groupId = mealPlanningDay.getUserGroup().getId();
        this.date = mealPlanningDay.getDate();
        this.weatherHighTemp = mealPlanningDay.getWeatherHighTemp();
        this.weatherLowTemp = mealPlanningDay.getWeatherLowTemp();
        this.weatherDescription = mealPlanningDay.getWeatherDescription();
        this.temperatureUnit = mealPlanningDay.getTemperatureUnit();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getWeatherHighTemp() {
        return weatherHighTemp;
    }

    public void setWeatherHighTemp(BigDecimal weatherHighTemp) {
        this.weatherHighTemp = weatherHighTemp;
    }

    public BigDecimal getWeatherLowTemp() {
        return weatherLowTemp;
    }

    public void setWeatherLowTemp(BigDecimal weatherLowTemp) {
        this.weatherLowTemp = weatherLowTemp;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(String temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }
}

