package com.homemenuplanner.controllers;

import com.homemenuplanner.dtos.ingredient.IngredientResponse;
import com.homemenuplanner.models.Ingredient;
import com.homemenuplanner.services.IngredientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;



    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;

    }

    @GetMapping
    public ResponseEntity<List<IngredientResponse>> getAllIngredients() {
        List<Ingredient> ingredients = ingredientService.findAll();
        return ResponseEntity.ok(ingredients.stream().map(ingredient -> new IngredientResponse(ingredient.getName(), ingredient.getPluralForm(), ingredient.getId())).toList());

    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientResponse> getIngredientById(@PathVariable Long id) {
        Optional<Ingredient> ingredient = ingredientService.findById(id);
        return ingredient.map(value -> ResponseEntity.ok(new IngredientResponse(value.getName(), value.getPluralForm(), value.getId()))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<IngredientResponse> createIngredient(@RequestBody Ingredient ingredient) {
        Ingredient savedIngredient = ingredientService.save(ingredient);
        return ResponseEntity.status(HttpStatus.CREATED).body(new IngredientResponse(savedIngredient.getName(), savedIngredient.getPluralForm(), savedIngredient.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ingredient> updateIngredient(@PathVariable Long id, @RequestBody Ingredient ingredient) {
        if (ingredientService.findById(id).isPresent()) {
            ingredient.setId(id);
            Ingredient updatedIngredient = ingredientService.save(ingredient);
            return ResponseEntity.ok(updatedIngredient);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id) {
        if (ingredientService.findById(id).isPresent()) {
            ingredientService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

