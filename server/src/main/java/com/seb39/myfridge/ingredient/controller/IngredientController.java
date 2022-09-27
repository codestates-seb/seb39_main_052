package com.seb39.myfridge.ingredient.controller;

import com.seb39.myfridge.dto.SingleResponseDto;
import com.seb39.myfridge.ingredient.service.IngredientService;
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
