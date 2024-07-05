package com.homemenuplanner.dtos.recipe;

import com.homemenuplanner.dtos.ingredient.RecipeIngredientRequest;

import java.util.List;

public class RecipeRequest {
    private String name;
    private String instructions;
    private String description;
    private String url;
    private Long cookbookId;
    private Integer page;
    private String imageFileName;
    private Long groupId;
    private Boolean isPublic;
    private List<RecipeIngredientRequest> ingredients;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public List<RecipeIngredientRequest> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredientRequest> ingredients) {
        this.ingredients = ingredients;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getCookbookId() {
        return cookbookId;
    }

    public void setCookbookId(Long cookbookId) {
        this.cookbookId = cookbookId;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }
}
