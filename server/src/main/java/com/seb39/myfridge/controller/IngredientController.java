package com.seb39.myfridge.controller;

import com.seb39.myfridge.domain.ingredient.service.IngredientService;
import com.seb39.myfridge.global.dto.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    @GetMapping("/api/ingredients/names")
    public ResponseEntity<SingleResponseDto<List<String>>> getNames(@RequestParam String word){
        List<String> ingredientNames = ingredientService.findNamesByContainsWord(word);
        return ResponseEntity.ok(new SingleResponseDto<>(ingredientNames));
    }
}