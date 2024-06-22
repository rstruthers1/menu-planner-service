package com.homemenuplanner.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "cookbooks")
public class Cookbook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int UNSIGNED not null")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "image_file_name")
    private String imageFileName;

    @OneToMany(mappedBy = "cookbookName", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Recipe> recipes;

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

    public Set<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(Set<Recipe> recipes) {
        this.recipes = recipes;
    }
}
