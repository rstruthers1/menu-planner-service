package com.homemenuplanner.dtos.cookbook;

public class CookbookResponse {
    private Long id;
    private String name;
    private String imageFileName;

    public CookbookResponse() {
    }

    public CookbookResponse(Long id, String name, String imageFileName) {
        this.id = id;
        this.name = name;
        this.imageFileName = imageFileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }
}
