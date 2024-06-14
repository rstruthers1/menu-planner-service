package com.homemenuplanner.services;

import com.homemenuplanner.models.Meal;
import com.homemenuplanner.models.Recipe;
import com.homemenuplanner.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.homemenuplanner.repositories.MealRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MealService {
    private final MealRepository mealRepository;
    private final RecipeRepository recipeRepository;

    @Autowired
    public MealService(MealRepository mealRepository,
                       RecipeRepository recipeRepository) {
        this.mealRepository = mealRepository;
        this.recipeRepository = recipeRepository;
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

    public Meal addRecipeToMeal(Long mealId, Long recipeId) {
        Optional<Meal> mealOptional = mealRepository.findById(mealId);
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (mealOptional.isPresent() && recipeOptional.isPresent()) {
            Meal meal = mealOptional.get();
            Recipe recipe = recipeOptional.get();

            meal.getRecipes().add(recipe);
            recipe.getMeals().add(meal);

            return mealRepository.save(meal);
        } else {
            // Handle case where meal or recipe is not found
            return null;
        }
    }
}
