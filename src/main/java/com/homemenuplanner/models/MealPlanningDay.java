package com.homemenuplanner.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "meal_planning_days")
public class MealPlanningDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int UNSIGNED not null")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private UserGroup userGroup;

    @Column(nullable = false)
    private String date;

    @Column(name = "weather_high_temp")
    private BigDecimal weatherHighTemp;

    @Column(name = "weather_low_temp")
    private BigDecimal weatherLowTemp;

    @Column(name = "weather_description")
    private String weatherDescription;

    @Column(name = "temperature_unit", nullable = false)
    private String temperatureUnit;

    @OneToMany(mappedBy = "mealPlanningDay", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealPlanningDayMeal> meals;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
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

    public List<MealPlanningDayMeal> getMeals() {
        return meals;
    }

    public void setMeals(List<MealPlanningDayMeal> meals) {
        this.meals = meals;
    }
}

