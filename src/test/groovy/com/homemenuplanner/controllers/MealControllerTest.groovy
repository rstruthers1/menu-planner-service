package com.homemenuplanner.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import spock.lang.Specification
import com.homemenuplanner.dtos.meal.CreateMealRequest

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MealControllerTest extends Specification {
    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @WithMockUser(username="admin")
    def "Test Add Meal Endpoint"() {
        given: "A valid meal request object"
        def mealRequest = new CreateMealRequest(
                "Test Meal",
                 "Test Description",
               "https://www.test.com"
        )

        when: "Sending a POST request to the add meal endpoint"
        def result = mockMvc.perform(MockMvcRequestBuilders.post("/api/meals")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(mealRequest)))

        then: "The response status is 201 CREATED"
        result.andExpect(MockMvcResultMatchers.status().isCreated())
        result.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
        result.andExpect(MockMvcResultMatchers.jsonPath('$.name').value("Test Meal"))
        result.andExpect(MockMvcResultMatchers.jsonPath('$.description').value("Test Description"))
        result.andExpect(MockMvcResultMatchers.jsonPath('$.url').value("https://www.test.com"))


    }
}