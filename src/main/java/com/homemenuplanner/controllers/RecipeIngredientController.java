package com.homemenuplanner.controllers;

import com.homemenuplanner.models.RecipeIngredient;
import com.homemenuplanner.services.RecipeIngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipe-ingredients")
public class RecipeIngredientController {

    private final RecipeIngredientService recipeIngredientService;

    @Autowired
    public RecipeIngredientController(RecipeIngredientService recipeIngredientService) {
        this.recipeIngredientService = recipeIngredientService;
    }

    @GetMapping
    public ResponseEntity<List<RecipeIngredient>> getAllRecipeIngredients() {
        List<RecipeIngredient> recipeIngredients = recipeIngredientService.findAll();
        return ResponseEntity.ok(recipeIngredients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeIngredient> getRecipeIngredientById(@PathVariable Long id) {
        Optional<RecipeIngredient> recipeIngredient = recipeIngredientService.findById(id);
        return recipeIngredient.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<RecipeIngredient> createRecipeIngredient(@RequestBody RecipeIngredient recipeIngredient) {
        RecipeIngredient savedRecipeIngredient = recipeIngredientService.save(recipeIngredient);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipeIngredient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeIngredient> updateRecipeIngredient(@PathVariable Long id, @RequestBody RecipeIngredient recipeIngredient) {
        if (recipeIngredientService.findById(id).isPresent()) {
            RecipeIngredient updatedRecipeIngredient = recipeIngredientService.save(recipeIngredient);
            return ResponseEntity.ok(updatedRecipeIngredient);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipeIngredient(@PathVariable Long id) {
        if (recipeIngredientService.findById(id).isPresent()) {
            recipeIngredientService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

