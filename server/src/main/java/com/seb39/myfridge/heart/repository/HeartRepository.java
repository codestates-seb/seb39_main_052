package com.seb39.myfridge.heart.repository;

import com.seb39.myfridge.heart.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart,Long> {
    boolean existsByMemberIdAndRecipeId(Long memberId, Long recipeId);
    Optional<Heart> findByMemberIdAndRecipeId(Long memberId, Long recipeId);
    int countByRecipeId(Long recipeId);
}
