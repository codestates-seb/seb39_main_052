package com.seb39.myfridge.domain.ingredient.Repository;

import java.util.List;

public interface IngredientRepositoryCustom {
    List<String> searchNames(String name);
}
