package com.homemenuplanner.services;

import com.homemenuplanner.dtos.mealplanningday.MealPlanningDayRequest;
import com.homemenuplanner.dtos.mealplanningday.MealPlanningDayResponse;
import com.homemenuplanner.dtos.mealplanningweek.MealPlanningWeekRequest;
import com.homemenuplanner.dtos.mealplanningweek.MealPlanningWeekResponse;
import com.homemenuplanner.models.MealPlanningDay;
import com.homemenuplanner.models.UserGroup;
import com.homemenuplanner.repositories.MealPlanningDayRepository;
import com.homemenuplanner.repositories.UserGroupRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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

    public MealPlanningWeekResponse getOrCreateMealPlanningDaysForWeek(MealPlanningWeekRequest request) {
        Long groupId = request.getGroupId();
        Optional<UserGroup> userGroup = userGroupRepository.findById(request.getGroupId());
        if (userGroup.isEmpty()) {
            throw new IllegalArgumentException("User group not found.");
        }

        String startDate = request.getStartDate();
        // startDate should be on Sunday
        // Convert startDate to LocalDate object
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(startDate);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid start date.");
        }

        // Get the day of the week of the startDate
        int dayOfWeek = localDate.getDayOfWeek().getValue();
        // validate that the day of the week is Sunday
        if (dayOfWeek != 7) {
            throw new IllegalArgumentException("Start date should be on Sunday.");
        }

        List<MealPlanningDay> mealPlanningDays = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            String mealPlanningDayDate = localDate.toString();
            Optional<MealPlanningDay> optionalMealPlanningDay = mealPlanningDayRepository.findByUserGroup_IdAndDate(groupId, mealPlanningDayDate);

            MealPlanningDay mealPlanningDay;
            if (optionalMealPlanningDay.isPresent()) {
                mealPlanningDays.add(optionalMealPlanningDay.get());
            } else {
                mealPlanningDay = new MealPlanningDay();
                mealPlanningDay.setUserGroup(userGroup.get());
                mealPlanningDay.setDate(mealPlanningDayDate);
                mealPlanningDays.add(mealPlanningDayRepository.save(mealPlanningDay));
            }

            // Add one day to the localDate
            localDate = localDate.plusDays(1);
        }

        List<MealPlanningDayResponse> responses = mealPlanningDays.stream()
                .map(MealPlanningDayResponse::new)
                .collect(Collectors.toList());

        MealPlanningWeekResponse response = new MealPlanningWeekResponse();
        response.setMealPlanningDays(responses);

        return response;
    }

}

