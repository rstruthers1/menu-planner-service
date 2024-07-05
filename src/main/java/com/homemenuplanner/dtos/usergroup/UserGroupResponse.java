package com.homemenuplanner.dtos.usergroup;


import com.homemenuplanner.dtos.user.UserResponse;

import java.util.List;

public class UserGroupResponse {
    private Long id;
    private String name;
    private List<UserResponse> users;

    // Getters and Setters
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

   public List<UserResponse> getUsers() {
        return users;
    }

    public void setUsers(List<UserResponse> users) {
        this.users = users;
    }
}

