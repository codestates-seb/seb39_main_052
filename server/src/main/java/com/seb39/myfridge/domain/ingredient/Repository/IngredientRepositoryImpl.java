package com.seb39.myfridge.domain.ingredient.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.seb39.myfridge.domain.ingredient.entity.QIngredient.ingredient;


@RequiredArgsConstructor
public class IngredientRepositoryImpl implements IngredientRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<String> searchNames(String name) {
        return queryFactory.select(ingredient.name)
                .from(ingredient)
                .where(ingredient.name.upper().contains(name.toUpperCase()))
                .limit(10)
                .fetch();
    }
}
