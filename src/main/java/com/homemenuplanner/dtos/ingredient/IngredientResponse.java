package com.homemenuplanner.dtos.ingredient;

import com.homemenuplanner.models.Ingredient;

import java.util.List;

public class IngredientResponse {

    private final String name;
    private final String pluralForm;
    private final long id;

    public IngredientResponse() {
        this.name = null;
        this.pluralForm = null;
        this.id = 0;
    }


    public IngredientResponse(String name, String pluralForm, long id) {
        this.name = name;
        this.pluralForm = pluralForm;
        this.id = id;
    }



    public String getName() {
        return name;
    }

    public String getPluralForm() {
        return pluralForm;
    }

    public long getId() {
        return id;
    }



}
