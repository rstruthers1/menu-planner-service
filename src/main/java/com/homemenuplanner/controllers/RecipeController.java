package com.homemenuplanner.controllers;

import com.homemenuplanner.dtos.recipe.RecipeRequest;
import com.homemenuplanner.dtos.recipe.RecipeResponse;
import com.homemenuplanner.services.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("")
    public ResponseEntity<RecipeResponse> addRecipe(@RequestBody RecipeRequest recipeRequest) {
        RecipeResponse savedRecipe = recipeService.addRecipe(recipeRequest);
        return new ResponseEntity<>(savedRecipe, HttpStatus.CREATED);
    }

    @GetMapping("")
    public List<RecipeResponse> getAllRecipes() {
        return recipeService.getAllRecipes();
    }
}
