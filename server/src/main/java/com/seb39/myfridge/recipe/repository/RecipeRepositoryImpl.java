package com.seb39.myfridge.recipe.repository;

import com.querydsl.codegen.ClassPathUtils;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.PathImpl;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seb39.myfridge.recipe.dto.QRecipeSearch_Response;
import com.seb39.myfridge.recipe.dto.RecipeDto;
import com.seb39.myfridge.recipe.dto.RecipeSearch;
import com.seb39.myfridge.recipe.entity.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.seb39.myfridge.heart.entity.QHeart.*;
import static com.seb39.myfridge.ingredient.entity.QIngredient.*;
import static com.seb39.myfridge.ingredient.entity.QRecipeIngredient.*;
import static com.seb39.myfridge.recipe.entity.QRecipe.*;

@RequiredArgsConstructor
public class RecipeRepositoryImpl implements RecipeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Value("${app.pageable.size.recipe}")
    private int size;

    @Override
    public List<String> searchTitles(String title) {
        return queryFactory
                .select(recipe.title)
                .from(recipe)
                .where(recipe.title.containsIgnoreCase(title))
                .limit(10)
                .fetch();
    }

    @Override
    public Page<RecipeSearch.Response> searchRecipes(RecipeSearch.Request request) {

        String title = request.getTitle();
        List<String> ingredientNames = request.getIngredients();
        int page = request.getPage();
        long offset = (long) (page - 1) * size;

        List<RecipeSearch.Response> content = queryFactory
                .select(
                        new QRecipeSearch_Response(
                                recipe.id,
                                recipe.title,
                                recipe.member.id,
                                recipe.member.name,
                                recipe.member.profileImagePath,
                                recipe.image.imagePath,
                                recipe.hearts.size(),
                                recipe.view,
                                recipe.lastModifiedAt
                        )
                )
                .from(recipe)
                .join(recipe.member)
                .leftJoin(recipe.image)
                .leftJoin(recipe.recipeIngredients, recipeIngredient)
                .leftJoin(recipeIngredient.ingredient, ingredient)
                .where(
                        recipe.title.containsIgnoreCase(title),
                        hasIngredientByNames(ingredientNames)
                )
                .groupBy(recipe.id)
                .having(hasAllIngredientsByNames(ingredientNames))
                .orderBy(recipeSearchOrderBy(request.getSortType()))
                .offset(offset)
                .limit(size)
                .fetch();

        return PageableExecutionUtils.getPage(content, PageRequest.of(page - 1, size), () -> searchRecipesCount(request));
    }

    private int searchRecipesCount(RecipeSearch.Request request) {
        String title = request.getTitle();
        List<String> ingredientNames = request.getIngredients();

        return queryFactory
                .select(recipe.count())
                .from(recipe)
                .leftJoin(recipe.recipeIngredients, recipeIngredient)
                .leftJoin(recipeIngredient.ingredient, ingredient)
                .where(
                        recipe.title.containsIgnoreCase(title),
                        hasIngredientByNames(ingredientNames)
                )
                .groupBy(recipe.id)
                .having(hasAllIngredientsByNames(ingredientNames))
                .fetch()
                .size();
    }

    private BooleanExpression hasIngredientByNames(List<String> names) {
        if (names == null || names.size() == 0)
            return null;
        return ingredient.name.in(names);
    }

    private OrderSpecifier recipeSearchOrderBy(RecipeSearch.SortType sortType) {
        switch (sortType) {
            case RECENT:
                return recipe.lastModifiedAt.desc();
            case VIEW:
                return recipe.view.desc();
            case HEART:
                return recipe.hearts.size().desc();
            default:
                return null;
        }
    }

    private Predicate hasAllIngredientsByNames(List<String> names) {
        if (names == null || names.size() == 0)
            return null;
        return recipeIngredient.count().eq((long) names.size());
    }
}
