package com.seb39.myfridge.heart.repository;

import com.seb39.myfridge.heart.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * join 불필요한거같은데 Native Query로 변경?
 */
public interface HeartRepository extends JpaRepository<Heart,Long> {
    boolean existsByMemberIdAndRecipeId(Long memberId, Long recipeId);
    Optional<Heart> findByMemberIdAndRecipeId(Long memberId, Long recipeId);
    int countByRecipeId(Long recipeId);
}
