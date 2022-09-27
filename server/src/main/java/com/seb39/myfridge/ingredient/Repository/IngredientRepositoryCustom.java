package com.seb39.myfridge.ingredient.Repository;

import java.util.List;

public interface IngredientRepositoryCustom {
    List<String> searchNames(String name);
}
