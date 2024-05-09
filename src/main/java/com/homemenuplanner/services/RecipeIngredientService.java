package com.homemenuplanner.services;

import com.homemenuplanner.models.RecipeIngredient;
import com.homemenuplanner.repositories.RecipeIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeIngredientService {

    private final RecipeIngredientRepository recipeIngredientRepository;

    @Autowired
    public RecipeIngredientService(RecipeIngredientRepository recipeIngredientRepository) {
        this.recipeIngredientRepository = recipeIngredientRepository;
    }

    public List<RecipeIngredient> findAll() {
        return recipeIngredientRepository.findAll();
    }

    public Optional<RecipeIngredient> findById(Long id) {
        return recipeIngredientRepository.findById(id);
    }

    public RecipeIngredient save(RecipeIngredient recipeIngredient) {
        return recipeIngredientRepository.save(recipeIngredient);
    }

    public void deleteById(Long id) {
        recipeIngredientRepository.deleteById(id);
    }
}
