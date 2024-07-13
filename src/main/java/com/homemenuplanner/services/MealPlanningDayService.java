package com.homemenuplanner.services;

import com.homemenuplanner.dtos.mealplanningday.MealPlanningDayRequest;
import com.homemenuplanner.dtos.mealplanningday.MealPlanningDayResponse;
import com.homemenuplanner.models.MealPlanningDay;
import com.homemenuplanner.models.UserGroup;
import com.homemenuplanner.repositories.MealPlanningDayRepository;
import com.homemenuplanner.repositories.UserGroupRepository;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MealPlanningDayService {

    private final MealPlanningDayRepository mealPlanningDayRepository;

    private final UserGroupRepository userGroupRepository;

    public MealPlanningDayService(MealPlanningDayRepository mealPlanningDayRepository, UserGroupRepository userGroupRepository) {
        this.mealPlanningDayRepository = mealPlanningDayRepository;
        this.userGroupRepository = userGroupRepository;
    }

    public MealPlanningDayResponse createMealPlanningDay(MealPlanningDayRequest request) {
        Optional<MealPlanningDay> existingDay = mealPlanningDayRepository.findByUserGroup_IdAndDate(request.getGroupId(), request.getDate());
        if (existingDay.isPresent()) {
            throw new IllegalArgumentException("Meal planning day for this group and date already exists.");
        }

        Optional<UserGroup> userGroup = userGroupRepository.findById(request.getGroupId());
        if (userGroup.isEmpty()) {
            throw new IllegalArgumentException("User group not found.");
        }

        MealPlanningDay mealPlanningDay = new MealPlanningDay();
        mealPlanningDay.setUserGroup(userGroup.get());
        mealPlanningDay.setDate(request.getDate());
        mealPlanningDay.setWeatherHighTemp(request.getWeatherHighTemp());
        mealPlanningDay.setWeatherLowTemp(request.getWeatherLowTemp());
        mealPlanningDay.setWeatherDescription(request.getWeatherDescription());
        mealPlanningDay.setTemperatureUnit(request.getTemperatureUnit());

        MealPlanningDay savedDay = mealPlanningDayRepository.save(mealPlanningDay);
        return new MealPlanningDayResponse(savedDay);
    }
}

