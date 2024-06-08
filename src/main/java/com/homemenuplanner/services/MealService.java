package com.homemenuplanner.services;

import com.homemenuplanner.models.Meal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.homemenuplanner.repositories.MealRepository;

import java.util.List;

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

    public List<Meal> findAll() {
        return mealRepository.findAll();
    }

    public Page<Meal> searchMealsByName(String name, Pageable pageable) {
        return mealRepository.findByNameContainingIgnoreCase(name, pageable);
    }
}
