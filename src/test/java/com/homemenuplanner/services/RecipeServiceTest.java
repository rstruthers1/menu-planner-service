package com.homemenuplanner.services;


import com.homemenuplanner.dtos.recipe.RecipeRequest;
import com.homemenuplanner.dtos.recipe.RecipeResponse;
import com.homemenuplanner.exceptions.ResourceNotFoundException;
import com.homemenuplanner.models.Recipe;
import com.homemenuplanner.repositories.CookbookRepository;
import com.homemenuplanner.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private CookbookRepository cookbookRepository;

    @InjectMocks
    private RecipeService recipeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddRecipe() {
        RecipeRequest recipeRequest = new RecipeRequest();
        recipeRequest.setName("Pancakes");
        recipeRequest.setInstructions("Mix ingredients and cook.");

        Recipe savedRecipe = new Recipe();
        savedRecipe.setId(1L);
        savedRecipe.setName("Pancakes");
        savedRecipe.setInstructions("Mix ingredients and cook.");

        when(recipeRepository.save(any(Recipe.class))).thenReturn(savedRecipe);

        RecipeResponse recipeResponse = recipeService.addRecipe(recipeRequest);

        assertEquals("Pancakes", recipeResponse.getName());
        assertEquals("Mix ingredients and cook.", recipeResponse.getInstructions());

        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }

    @Test
    public void testUpdateRecipe() {
        RecipeRequest recipeRequest = new RecipeRequest();
        recipeRequest.setName("Waffles");
        recipeRequest.setInstructions("Mix ingredients and cook.");

        Recipe existingRecipe = new Recipe();
        existingRecipe.setId(1L);
        existingRecipe.setName("Old Recipe");
        existingRecipe.setInstructions("Old instructions.");

        Recipe updatedRecipe = new Recipe();
        updatedRecipe.setId(1L);
        updatedRecipe.setName("Waffles");
        updatedRecipe.setInstructions("Mix ingredients and cook.");

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(existingRecipe));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(updatedRecipe);

        RecipeResponse recipeResponse = recipeService.updateRecipe(1L, recipeRequest);

        assertEquals("Waffles", recipeResponse.getName());
        assertEquals("Mix ingredients and cook.", recipeResponse.getInstructions());

        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }

    @Test
    public void testUpdateRecipeNotFound() {
        RecipeRequest recipeRequest = new RecipeRequest();
        recipeRequest.setName("Waffles");
        recipeRequest.setInstructions("Mix ingredients and cook.");

        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> recipeService.updateRecipe(1L, recipeRequest));

        verify(recipeRepository, never()).save(any(Recipe.class));
    }
}
