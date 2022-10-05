package com.seb39.myfridge.fridge.repository;

import com.seb39.myfridge.fridge.entity.FridgeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FridgeIngredientRepository extends JpaRepository<FridgeIngredient, Long> {

    @Query("select fi from FridgeIngredient fi join fetch fi.ingredient where fi.fridge.id = :id")
    List<FridgeIngredient> findFridgeIngredientsByFridgeId(@Param("id") Long id);

    void deleteAllByFridgeId(Long id);
}
