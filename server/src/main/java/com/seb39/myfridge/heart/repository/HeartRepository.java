package com.seb39.myfridge.heart.repository;

import com.seb39.myfridge.heart.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface HeartRepository extends JpaRepository<Heart,Long> {
    boolean existsByMemberIdAndRecipeId(Long memberId, Long recipeId);
    Optional<Heart> findByMemberIdAndRecipeId(Long memberId, Long recipeId);
    int countByRecipeId(Long recipeId);
    @Query("select h from Heart h where h.member.id = :memberId and h.recipe.id in :recipes")
    List<Heart> findByMemberIdAndRecipeIds(@Param("memberId") Long memberId, @Param("recipes") List<Long> recipeIds);

}
