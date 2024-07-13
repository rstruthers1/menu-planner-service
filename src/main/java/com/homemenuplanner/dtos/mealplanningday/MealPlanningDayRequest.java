package com.homemenuplanner.dtos.mealplanningday;


import java.math.BigDecimal;

public class MealPlanningDayRequest {
    private Long groupId;
    private String date;
    private BigDecimal weatherHighTemp;
    private BigDecimal weatherLowTemp;
    private String weatherDescription;
    private String temperatureUnit;

    // Getters and Setters

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

