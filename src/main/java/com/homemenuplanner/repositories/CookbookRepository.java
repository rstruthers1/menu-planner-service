package com.homemenuplanner.repositories;

import com.homemenuplanner.models.Cookbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CookbookRepository extends JpaRepository<Cookbook, Long> {
    Page<Cookbook> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
