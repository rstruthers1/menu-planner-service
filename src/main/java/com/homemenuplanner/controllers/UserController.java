package com.homemenuplanner.controllers;

import com.homemenuplanner.dtos.user.GetUserResponse;
import com.homemenuplanner.dtos.user.UpdateUserRequest;
import com.homemenuplanner.models.User;
import com.homemenuplanner.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserResponse> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new GetUserResponse(user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GetUserResponse> updateUserById(@PathVariable Long id, @RequestBody UpdateUserRequest updateUserRequest) {
        User user = new User(updateUserRequest.getEmail(), updateUserRequest.getFirstName(), updateUserRequest.getLastName());
        User updatedUser = userService.updateUserById(id, user);
        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new GetUserResponse(updatedUser.getId(),
                updatedUser.getEmail(),
                updatedUser.getFirstName(),
                updatedUser.getLastName()));
    }
}
