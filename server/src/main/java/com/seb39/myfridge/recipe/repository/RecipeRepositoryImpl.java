package com.seb39.myfridge.recipe.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seb39.myfridge.heart.entity.QHeart;
import com.seb39.myfridge.ingredient.entity.QRecipeIngredient;
import com.seb39.myfridge.member.entity.QMember;
import com.seb39.myfridge.recipe.dto.RecipeDto;
import com.seb39.myfridge.recipe.dto.RecipeSearch;
import com.seb39.myfridge.recipe.entity.QRecipe;
import com.seb39.myfridge.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.hibernate.criterion.LikeExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;

import static com.seb39.myfridge.heart.entity.QHeart.*;
import static com.seb39.myfridge.ingredient.entity.QRecipeIngredient.*;
import static com.seb39.myfridge.recipe.entity.QRecipe.*;

@RequiredArgsConstructor
public class RecipeRepositoryImpl implements RecipeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<String> searchTitles(String title) {
        return queryFactory
                .select(recipe.title)
                .from(recipe)
                .where(
                        titleContainsIgnoreCase(title)
                )
                .limit(10)
                .fetch();
    }

    @Override
    public List<RecipeDto.Response> searchRecipes(RecipeSearch recipeSearch) {
        /**
         * TODO
         *  1. Paging
         *  2. image 추가
         *  3. where - ingredients
         */

        ConstructorExpression<RecipeDto.Response> dtoProjection = Projections.constructor(RecipeDto.Response.class,
                recipe.id,
                recipe.title,
                recipe.member.id,
                recipe.member.name,
                recipe.member.profileImagePath,
                null,
                ExpressionUtils.as(
                        JPAExpressions.select(heart.count().intValue())
                                .from(heart)
                                .where(heart.recipe.eq(recipe))
                        , "heartCounts"),
                recipe.view
        );

        // Ingredients 어떻게?

        return queryFactory
                .select(dtoProjection)
                .from(recipe)
                .join(recipe.member)
                .where(titleContainsIgnoreCase(recipeSearch.getTitle()))
                .fetch();
    }

    private BooleanExpression titleContainsIgnoreCase(String title){
        return recipe.title.upper().contains(title.toUpperCase());
    }
}
