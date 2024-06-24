package com.homemenuplanner.controllers;

import com.homemenuplanner.dtos.recipe.RecipeRequest;
import com.homemenuplanner.dtos.recipe.RecipeResponse;
import com.homemenuplanner.models.Recipe;
import com.homemenuplanner.services.RecipeService;
import com.homemenuplanner.dtos.cookbook.CookbookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/search")
    public ResponseEntity<Page<RecipeResponse>> searchRecipesByName(@RequestParam(name="name") String name, Pageable pageable) {
        Page<Recipe> recipes = recipeService.searchRecipesByName(name, pageable);
        Page<RecipeResponse> recipeResponses = recipes.map(recipe -> {
            CookbookResponse cookbookResponse = null;
                if (recipe.getCookbook() != null) {
                    cookbookResponse = new CookbookResponse();
                    cookbookResponse.setId(recipe.getCookbook().getId());
                    cookbookResponse.setName(recipe.getCookbook().getName());
                    cookbookResponse.setImageFileName(recipe.getCookbook().getImageFileName());
                }

                return new RecipeResponse(
                        recipe.getId(),
                        recipe.getName(),
                        recipe.getInstructions(),
                        recipe.getDescription(),
                        cookbookResponse,
                        recipe.getPage(),
                        recipe.getUrl(),
                        recipe.getImageFileName(),
                        null);
        });
        return new ResponseEntity<>(recipeResponses, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeResponse> updateRecipe(@PathVariable(name="id") Long id, @RequestBody RecipeRequest recipeRequest) {
        RecipeResponse updatedRecipe = recipeService.updateRecipe(id, recipeRequest);
        return new ResponseEntity<>(updatedRecipe, HttpStatus.OK);
    }
}
