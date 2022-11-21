package com.seb39.myfridge.domain.fridge.repository;

import com.seb39.myfridge.domain.fridge.entity.FridgeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FridgeIngredientRepository extends JpaRepository<FridgeIngredient, Long> {

    @Query("select fi from FridgeIngredient fi join fetch fi.ingredient where fi.fridge.id = :id")
    List<FridgeIngredient> findFridgeIngredientsByFridgeId(@Param("id") Long id);

    void deleteAllByFridgeId(Long id);
}
