package com.seb39.myfridge.fridge.repository;

import com.seb39.myfridge.fridge.entity.FridgeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FridgeIngredientRepository extends JpaRepository<FridgeIngredient, Long> {


    List<FridgeIngredient> findFridgeIngredientsByFridgeId(Long id);

    void deleteAllByFridgeId(Long id);

    List<FridgeIngredient> findFridgeIngredientByFridgeIdOrderByExpirationAsc(Long id);

}
