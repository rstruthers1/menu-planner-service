package com.homemenuplanner.repositories;

import com.homemenuplanner.models.MealPlanningDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MealPlanningDayRepository extends JpaRepository<MealPlanningDay, Long> {
    Optional<MealPlanningDay> findByUserGroup_IdAndDate(Long groupId, String date);
}

