package com.homemenuplanner.repositories;

import com.homemenuplanner.models.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Page<Recipe> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT r FROM Recipe r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%')) AND (r.userGroup.id = :groupId OR r.isPublic = true)")
    Page<Recipe> findByNameContainingIgnoreCaseAndUserGroupOrIsPublic(@Param("name") String name, @Param("groupId") Long groupId, Pageable pageable);

    @Query("SELECT r FROM Recipe r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%')) AND r.userGroup.id = :groupId")
    Page<Recipe> findByNameContainingIgnoreCaseAndUserGroup(@Param("name") String name, @Param("groupId") Long groupId, Pageable pageable);

    @Query("SELECT r FROM Recipe r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%')) AND r.isPublic = true")
    Page<Recipe> findByNameContainingIgnoreCaseAndIsPublic(@Param("name") String name, Pageable pageable);

}
