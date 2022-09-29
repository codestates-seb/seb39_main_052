package com.seb39.myfridge.fridge.controller;

import com.seb39.myfridge.auth.annotation.AuthMemberId;
import com.seb39.myfridge.fridge.dto.FridgeDto;
import com.seb39.myfridge.fridge.entity.Fridge;
import com.seb39.myfridge.fridge.entity.FridgeIngredient;
import com.seb39.myfridge.fridge.mapper.FridgeMapper;
import com.seb39.myfridge.fridge.service.FridgeIngredientService;
import com.seb39.myfridge.fridge.service.FridgeService;
import com.seb39.myfridge.ingredient.entity.Ingredient;
import com.seb39.myfridge.ingredient.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/fridge")
@RequiredArgsConstructor
public class FridgeIngredientController {

    private final FridgeService fridgeService;

    private final FridgeMapper fridgeMapper;


    private final FridgeIngredientService fridgeIngredientService;

    //Post요청으로 모두 처리가능
    //냉장고 비우기 -> 배열 안에 있는 항목을 모두 지우고 POST
    //냉장고 수정 -> 배열 안에 있는 항목을 모두 지우고 새로운 요청 데이터 POST
    @PostMapping
    public ResponseEntity postFridgeIngredient(@Valid @RequestBody FridgeDto.Post requestBody,
                                               @AuthMemberId Long memberId) {
        Fridge fridge = fridgeService.findFridge(memberId);
        List<FridgeIngredient> fridgeIngredients = fridgeIngredientService.createFridgeIngredient(fridge, requestBody);
        FridgeDto.Response response = fridgeMapper.fridgeIngredientsToResponse(fridgeIngredients);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getFridgeIngredient(@AuthMemberId Long memberId) {
        Fridge fridge = fridgeService.findFridge(memberId);
        List<FridgeIngredient> fridgeIngredient = fridgeIngredientService.findFridgeIngredient(fridge.getId());
        FridgeDto.Response response = fridgeMapper.fridgeIngredientsToResponse(fridgeIngredient);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity deleteFridgeIngredient(@AuthMemberId Long memberId) {
        Fridge fridge = fridgeService.findFridge(memberId);
        fridgeIngredientService.deleteFridgeIngredient(fridge.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
