package com.homemenuplanner.controllers;

import com.homemenuplanner.dtos.meal.CreateMealRequest;
import com.homemenuplanner.dtos.meal.CreateMealResponse;
import com.homemenuplanner.models.Meal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.homemenuplanner.services.MealService;

@RestController
@RequestMapping("/api/meals")
public class MealController {
    private final MealService mealService;

    @Autowired
    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @PostMapping("")
    public ResponseEntity<CreateMealResponse> addMeal(@RequestBody CreateMealRequest createMealRequest) {
        Meal meal = new Meal(createMealRequest.getName(), createMealRequest.getUrl(), createMealRequest.getDescription());
        Meal savedMeal = mealService.save(meal);
        CreateMealResponse createMealResponse = new CreateMealResponse(savedMeal.getId(), savedMeal.getName(), savedMeal.getDescription(), savedMeal.getUrl());
        return new ResponseEntity<>(createMealResponse, HttpStatus.CREATED);
    }
}
