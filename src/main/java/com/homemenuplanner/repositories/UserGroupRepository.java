package com.homemenuplanner.repositories;

import com.homemenuplanner.models.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    UserGroup findByName(String name);
    Optional<UserGroup> findById(Long id);
}
