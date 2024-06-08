package com.homemenuplanner.dtos.user;

public class UpdateUserRequest {
    private String email;
    private String firstName;
    private String lastName;

    public UpdateUserRequest(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
