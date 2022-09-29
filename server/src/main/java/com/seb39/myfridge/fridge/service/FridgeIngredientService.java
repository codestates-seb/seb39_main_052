package com.seb39.myfridge.fridge.service;

import com.seb39.myfridge.fridge.dto.FridgeDto;
import com.seb39.myfridge.fridge.entity.Fridge;
import com.seb39.myfridge.fridge.entity.FridgeIngredient;
import com.seb39.myfridge.fridge.repository.FridgeIngredientRepository;
import com.seb39.myfridge.ingredient.entity.Ingredient;
import com.seb39.myfridge.ingredient.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FridgeIngredientService {

    private final FridgeIngredientRepository fridgeIngredientRepository;
    private final IngredientService ingredientService;


    @Transactional
    public List<FridgeIngredient> createFridgeIngredient(Fridge fridge, FridgeDto.Post requestBody) {
        fridgeIngredientRepository.deleteAllByFridgeId(fridge.getId());
        List<FridgeIngredient> fridgeIngredientList = new ArrayList<>();
        List<FridgeDto.IngredientInfo> requestBodyFridgeIngredients = requestBody.getFridgeIngredients();
        for (FridgeDto.IngredientInfo requestBodyFridgeIngredient : requestBodyFridgeIngredients) {
            if (!ingredientService.isIngredientExist(requestBodyFridgeIngredient.getName())) {
                ingredientService.createIngredient(requestBodyFridgeIngredient.getName());
            }
            Ingredient ingredient = ingredientService.findIngredient(requestBodyFridgeIngredient.getName());
            FridgeIngredient fridgeIngredient = FridgeIngredient.createNewFridgeIngredient(fridge, requestBodyFridgeIngredient, ingredient);
            fridgeIngredientRepository.save(fridgeIngredient);
            fridgeIngredientList.add(fridgeIngredient);
        }

        return fridgeIngredientList;
    }

    //냉장고 식별자로 fridgeIngredient 찾기
    //해당 냉장고에 어떤 재료가 있는지 확인
    public List<FridgeIngredient> findFridgeIngredient(Long fridgeId) {
        return fridgeIngredientRepository.findFridgeIngredientsByFridgeId(fridgeId);
    }

    @Transactional
    public void deleteFridgeIngredient(Long fridgeId) {
        fridgeIngredientRepository.deleteAllByFridgeId(fridgeId);
    }

    public List<FridgeIngredient> findFridgeIngredientOrderByExpiration(Long fridgeId) {
        return fridgeIngredientRepository.findFridgeIngredientByFridgeIdOrderByExpirationAsc(fridgeId);
    }
}
