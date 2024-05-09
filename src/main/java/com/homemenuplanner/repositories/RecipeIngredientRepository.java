package com.homemenuplanner.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.homemenuplanner.models.RecipeIngredient;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
}

