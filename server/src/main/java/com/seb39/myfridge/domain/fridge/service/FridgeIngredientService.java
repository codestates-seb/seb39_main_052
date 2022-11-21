package com.seb39.myfridge.domain.fridge.service;

import com.seb39.myfridge.domain.fridge.dto.FridgeDto;
import com.seb39.myfridge.domain.fridge.entity.Fridge;
import com.seb39.myfridge.domain.fridge.entity.FridgeIngredient;
import com.seb39.myfridge.domain.fridge.repository.FridgeIngredientRepository;
import com.seb39.myfridge.domain.ingredient.entity.Ingredient;
import com.seb39.myfridge.domain.ingredient.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        List<FridgeIngredient> fridgeIngredients = fridgeIngredientRepository.findFridgeIngredientsByFridgeId(fridgeId);
        fridgeIngredients = fridgeIngredients.stream().sorted(Comparator.comparing(FridgeIngredient::getExpiration)).collect(Collectors.toList());
        return fridgeIngredients;
    }

    @Transactional
    public void deleteFridgeIngredient(Long fridgeId) {
        fridgeIngredientRepository.deleteAllByFridgeId(fridgeId);
    }

}
