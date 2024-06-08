package com.homemenuplanner.repositories;

import com.homemenuplanner.models.Meal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    Page<Meal> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
