package com.seb39.myfridge.domain.fridge.mapper;

import com.seb39.myfridge.domain.fridge.dto.FridgeDto;
import com.seb39.myfridge.domain.fridge.entity.FridgeIngredient;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface FridgeMapper {

    default List<FridgeDto.IngredientInfoResponse> fridgeIngredientsToFridgeIngredientsDto(List<FridgeIngredient> ingredients) {
        List<FridgeDto.IngredientInfoResponse> ingredientInfoResponses = new ArrayList<>();
        for (FridgeIngredient ingredient : ingredients) {
            FridgeDto.IngredientInfoResponse response = FridgeDto.IngredientInfoResponse.builder()
                    .id(ingredient.getId())
                    .name(ingredient.getIngredient().getName())
                    .quantity(ingredient.getQuantity())
                    .note(ingredient.getNote())
                    .expiration(ingredient.getExpiration())
                    .build();
            ingredientInfoResponses.add(response);
        }
        return ingredientInfoResponses;
    }


    default FridgeDto.Response fridgeIngredientsToResponse(List<FridgeIngredient> fridgeIngredients) {
        return FridgeDto.Response.builder().fridgeIngredients(fridgeIngredientsToFridgeIngredientsDto(fridgeIngredients)).build();
    }

    default FridgeDto.IngredientInfo fridgeIngredientToFridgeIngredientDto(FridgeIngredient fridgeIngredient) {
        return FridgeDto.IngredientInfo
                .builder()
                .quantity(fridgeIngredient.getQuantity())
                .name(fridgeIngredient.getIngredient().getName())
                .expiration(fridgeIngredient.getExpiration())
                .note(fridgeIngredient.getNote())
                .build();
    }

}
