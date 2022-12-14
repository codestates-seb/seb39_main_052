package com.seb39.myfridge.step.repository;

import com.seb39.myfridge.step.entity.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StepRepository extends JpaRepository<Step, Long> {

    @Modifying
    @Query("delete from Step s where s.recipe.id = :id")
    void deleteStepByRecipeId(@Param("id") Long id);
}
