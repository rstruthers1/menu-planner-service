package com.homemenuplanner.services;

import com.homemenuplanner.dtos.user.UserResponse;
import com.homemenuplanner.dtos.usergroup.UserGroupRequest;
import com.homemenuplanner.dtos.usergroup.UserGroupResponse;
import com.homemenuplanner.models.User;
import com.homemenuplanner.models.UserGroup;
import com.homemenuplanner.repositories.UserGroupRepository;
import com.homemenuplanner.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserGroupService {
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;

    public UserGroupService(UserGroupRepository userGroupRepository, UserRepository userRepository) {
        this.userGroupRepository = userGroupRepository;
        this.userRepository = userRepository;
    }

    public UserGroupResponse addGroup(UserGroupRequest userGroupRequest, String username) {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        UserGroup userGroup = new UserGroup();
        userGroup.setName(userGroupRequest.getName());
        UserGroup savedGroup = userGroupRepository.save(userGroup);

        savedGroup.getUsers().add(user);
        user.getUserGroups().add(savedGroup);
        userRepository.save(user);


        return convertToDto(savedGroup);
    }

    public Optional<UserGroupResponse> updateGroup(Long groupId, UserGroupRequest userGroupRequest) {
        return userGroupRepository.findById(groupId).map(userGroup -> {
            userGroup.setName(userGroupRequest.getName());
            UserGroup updatedGroup = userGroupRepository.save(userGroup);
            return convertToDto(updatedGroup);
        });
    }

    public List<UserGroupResponse> listAllGroups() {
        return userGroupRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<UserGroupResponse> listUsersGroups(String username) {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user.getUserGroups().stream()
                .sorted(Comparator.comparing(UserGroup::getName))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<UserGroupResponse> addMemberToGroup(Long groupId, Long userId) {
        Optional<UserGroup> groupOpt = userGroupRepository.findById(groupId);
        Optional<User> userOpt = userRepository.findById(userId);
        if (groupOpt.isPresent() && userOpt.isPresent()) {
            UserGroup group = groupOpt.get();
            User user = userOpt.get();
            group.getUsers().add(user);
            return Optional.of(convertToDto(group));
        }
        return Optional.empty();
    }

    public Optional<Set<User>> listMembersInGroup(Long groupId) {
        return userGroupRepository.findById(groupId).map(UserGroup::getUsers);
    }

    @Transactional
    public Optional<UserGroupResponse> deleteMemberFromGroup(Long groupId, Long userId) {
        Optional<UserGroup> groupOpt = userGroupRepository.findById(groupId);
        Optional<User> userOpt = userRepository.findById(userId);
        if (groupOpt.isPresent() && userOpt.isPresent()) {
            UserGroup group = groupOpt.get();
            User user = userOpt.get();
            group.getUsers().remove(user);
            return Optional.of(convertToDto(group));
        }
        return Optional.empty();
    }

    private UserGroupResponse convertToDto(UserGroup userGroup) {
        UserGroupResponse userGroupResponse = new UserGroupResponse();
        userGroupResponse.setId(userGroup.getId());
        userGroupResponse.setName(userGroup.getName());
        userGroupResponse.setUsers(userGroup.getUsers().stream().map(user -> new UserResponse(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName())).collect(Collectors.toList()));
        return userGroupResponse;
    }
}
