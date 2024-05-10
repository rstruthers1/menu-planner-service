package com.homemenuplanner.services;

import com.homemenuplanner.models.Meal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.homemenuplanner.repositories.MealRepository;

@Service
public class MealService {
    private final MealRepository mealRepository;

    @Autowired
    public MealService(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    public Meal save(Meal meal) {
        return mealRepository.save(meal);
    }

}
