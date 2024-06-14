package com.homemenuplanner.controllers;

import com.homemenuplanner.dtos.meal.CreateMealRequest;
import com.homemenuplanner.dtos.meal.MealResponse;
import com.homemenuplanner.models.Meal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.homemenuplanner.services.MealService;
import java.util.List;

@RestController
@RequestMapping("/api/meals")
public class MealController {
    private final MealService mealService;

    @Autowired
    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @PostMapping("")
    public ResponseEntity<MealResponse> addMeal(@RequestBody CreateMealRequest createMealRequest) {
        Meal meal = new Meal(createMealRequest.getName(), createMealRequest.getUrl(), createMealRequest.getDescription());
        Meal savedMeal = mealService.save(meal);
        MealResponse mealResponse = new MealResponse(savedMeal.getId(), savedMeal.getName(), savedMeal.getDescription(), savedMeal.getUrl());
        return new ResponseEntity<>(mealResponse, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<Meal>> getAllMeals() {
        List<Meal> meals = mealService.findAll();
        return new ResponseEntity<>(meals, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<MealResponse>> searchMealsByName(@RequestParam String name, Pageable pageable) {
        Page<Meal> meals = mealService.searchMealsByName(name, pageable);
        Page<MealResponse> mealResponses = meals.map(meal -> new MealResponse(meal.getId(), meal.getName(), meal.getDescription(), meal.getUrl()));
        return new ResponseEntity<>(mealResponses, HttpStatus.OK);
    }

    @PostMapping("/{mealId}/recipes/{recipeId}")
    public ResponseEntity<?> addRecipeToMeal(@PathVariable("mealId") Long mealId, @PathVariable("recipeId") Long recipeId) {
        Meal meal = mealService.addRecipeToMeal(mealId, recipeId);

        if (meal != null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
