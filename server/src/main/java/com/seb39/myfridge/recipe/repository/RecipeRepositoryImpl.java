package com.seb39.myfridge.recipe.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RecipeRepositoryImpl {
    private final JPAQueryFactory queryFactory;

}
