package com.seb39.myfridge.fridge.entity;

import com.seb39.myfridge.fridge.dto.FridgeDto;
import com.seb39.myfridge.ingredient.entity.Ingredient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class FridgeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fridge_ingredient_id")
    private Long id;

    private String quantity;

    private LocalDate expiration;

    private String note;

    @ManyToOne
    @JoinColumn(name = "fridge_id")
    private Fridge fridge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    //생성 메서드

    public static FridgeIngredient createNewFridgeIngredient(Fridge fridge, FridgeDto.IngredientInfo ingredientInfo, Ingredient ingredient) {
        FridgeIngredient fridgeIngredient = new FridgeIngredient();
        fridgeIngredient.setIngredient(ingredient);
        fridgeIngredient.setFridge(fridge);
        fridgeIngredient.setNote(ingredientInfo.getNote());
        fridgeIngredient.setExpiration(ingredientInfo.getExpiration());
        fridgeIngredient.setQuantity(ingredientInfo.getQuantity());
        return fridgeIngredient;
    }
}
