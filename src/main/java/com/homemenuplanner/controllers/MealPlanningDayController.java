package com.homemenuplanner.controllers;

import com.homemenuplanner.dtos.mealplanningweek.MealPlanningWeekRequest;
import com.homemenuplanner.dtos.mealplanningweek.MealPlanningWeekResponse;
import com.homemenuplanner.services.MealPlanningDayService;
import com.homemenuplanner.dtos.mealplanningday.MealPlanningDayRequest;
import com.homemenuplanner.dtos.mealplanningday.MealPlanningDayResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meal-planning-days")
public class MealPlanningDayController {

    private final MealPlanningDayService mealPlanningDayService;

    public MealPlanningDayController(MealPlanningDayService mealPlanningDayService) {
        this.mealPlanningDayService = mealPlanningDayService;
    }

    @PostMapping("")
    public ResponseEntity<MealPlanningDayResponse> createMealPlanningDay(@RequestBody MealPlanningDayRequest request) {
        MealPlanningDayResponse response = mealPlanningDayService.createMealPlanningDay(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/week")
    public ResponseEntity<MealPlanningWeekResponse> getOrCreateMealPlanningDaysForWeek(@RequestBody MealPlanningWeekRequest request) {
        MealPlanningWeekResponse response = mealPlanningDayService.getOrCreateMealPlanningDaysForWeek(request);
        return ResponseEntity.ok(response);
    }
}
