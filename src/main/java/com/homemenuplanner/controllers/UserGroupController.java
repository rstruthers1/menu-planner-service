package com.homemenuplanner.controllers;

import com.homemenuplanner.dtos.usergroup.UserGroupRequest;
import com.homemenuplanner.dtos.usergroup.UserGroupResponse;
import com.homemenuplanner.models.User;
import com.homemenuplanner.services.UserGroupService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/usergroups")
public class UserGroupController {

    private final UserGroupService userGroupService;

    public UserGroupController(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

    @PostMapping
    public ResponseEntity<UserGroupResponse> createGroup(@RequestBody UserGroupRequest userGroupRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        System.out.println("username: " + username);

        UserGroupResponse createdGroup = userGroupService.addGroup(userGroupRequest, username);
        return ResponseEntity.ok(createdGroup);
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<UserGroupResponse> updateGroup(@PathVariable Long groupId, @RequestBody UserGroupRequest userGroupRequest) {
        Optional<UserGroupResponse> updatedGroup = userGroupService.updateGroup(groupId, userGroupRequest);
        return updatedGroup.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<UserGroupResponse>> getAllGroups() {
        List<UserGroupResponse> groups = userGroupService.listAllGroups();
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/mygroups")
    public ResponseEntity<List<UserGroupResponse>> getUsersGroups() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        List<UserGroupResponse> groups = userGroupService.listUsersGroups(username);
        return ResponseEntity.ok(groups);
    }

    @PostMapping("/{groupId}/members/{userId}")
    public ResponseEntity<UserGroupResponse> addMemberToGroup(@PathVariable(name="groupId") Long groupId, @PathVariable(name="userId") Long userId) {
        Optional<UserGroupResponse> group = userGroupService.addMemberToGroup(groupId, userId);
        return group.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<Set<User>> getMembersInGroup(@PathVariable(name="groupId") Long groupId) {
        Optional<Set<User>> members = userGroupService.listMembersInGroup(groupId);
        return members.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{groupId}/members/{userId}")
    public ResponseEntity<UserGroupResponse> removeMemberFromGroup(@PathVariable(name="groupId") Long groupId, @PathVariable(name="userId") Long userId) {
        Optional<UserGroupResponse> group = userGroupService.deleteMemberFromGroup(groupId, userId);
        return group.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
