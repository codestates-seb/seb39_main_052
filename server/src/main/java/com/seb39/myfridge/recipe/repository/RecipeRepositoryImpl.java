package com.seb39.myfridge.recipe.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seb39.myfridge.recipe.dto.*;
import com.seb39.myfridge.recipe.enums.RecipeSort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.seb39.myfridge.heart.entity.QHeart.*;
import static com.seb39.myfridge.ingredient.entity.QIngredient.*;
import static com.seb39.myfridge.ingredient.entity.QRecipeIngredient.*;
import static com.seb39.myfridge.member.entity.QMember.*;
import static com.seb39.myfridge.recipe.entity.QRecipe.*;

@RequiredArgsConstructor
public class RecipeRepositoryImpl implements RecipeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Value("${app.pageable.size.recipe}")
    private int searchSize;

    @Value("${app.pageable.size.my-recipe}")
    private int myRecipeSize;

    @Override
    public List<String> searchTitles(String title) {
        return queryFactory
                .select(recipe.title)
                .from(recipe)
                .where(recipe.title.containsIgnoreCase(title))
                .distinct()
                .limit(10)
                .fetch();
    }

    //region search
    @Override
    public Page<RecipeSearch.Response> searchRecipes(RecipeSearch.Request request) {

        String title = request.getTitle();
        List<String> ingredientNames = request.getIngredients();
        int page = request.getPage();

        long offset = (long) (page - 1) * searchSize;

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
                .orderBy(recipeSearchOrderBy(request.getSort()))
                .offset(offset)
                .limit(searchSize)
                .fetch();

        return PageableExecutionUtils.getPage(content, PageRequest.of(page - 1, searchSize), () -> searchRecipesCount(request));
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

    private OrderSpecifier recipeSearchOrderBy(RecipeSort sort) {
        switch (sort) {
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
    //endregion

    //region myRecipe
    @Override
    public Page<MyRecipeDto.Mine> findMyRecipes(Long memberId, int page, RecipeSort sort) {

        long offset = (long) (page - 1) * myRecipeSize;

        List<MyRecipeDto.Mine> content = queryFactory
                .select(
                        new QMyRecipeDto_Mine(
                                recipe.id,
                                recipe.title,
                                recipe.image.imagePath,
                                recipe.view,
                                recipe.hearts.size(),
                                recipe.comments.size(),
                                recipe.lastModifiedAt
                        )
                )
                .from(recipe)
                .join(recipe.member)
                .leftJoin(recipe.image)
                .where(
                        recipe.member.id.eq(memberId)
                )
                .orderBy(recipeSearchOrderBy(sort))
                .offset(offset)
                .limit(myRecipeSize)
                .fetch();

        return PageableExecutionUtils.getPage(content, PageRequest.of(page - 1, myRecipeSize), () -> findMyRecipesCount(memberId));
    }

    private int findMyRecipesCount(Long memberId) {
        Integer count = queryFactory
                .select(recipe.count().intValue())
                .from(recipe)
                .join(recipe.member)
                .where(
                        recipe.member.id.eq(memberId)
                )
                .fetchOne();
        return count != null ? count : 0;
    }

    @Override
    public Page<MyRecipeDto.Favorite> findFavoriteRecipes(Long memberId, int page) {

        List<Long> recipeIds = findFavoriteRecipeIds(memberId, page);

        List<MyRecipeDto.Favorite> dtos = queryFactory
                .select(
                        new QMyRecipeDto_Favorite(
                                recipe.id,
                                recipe.title,
                                recipe.image.imagePath,
                                recipe.member.id,
                                recipe.member.name,
                                recipe.member.profileImagePath,
                                recipe.lastModifiedAt
                        )
                )
                .from(recipe)
                .join(recipe.member)
                .leftJoin(recipe.image)
                .where(
                        recipe.id.in(recipeIds)
                )
                .fetch();

        Map<Long, MyRecipeDto.Favorite> map = dtos.stream()
                .collect(Collectors.toMap(dto -> dto.getId(), dto -> dto));

        List<MyRecipeDto.Favorite> content = recipeIds.stream()
                .map(map::get)
                .collect(Collectors.toList());

        return PageableExecutionUtils.getPage(content, PageRequest.of(page - 1, myRecipeSize), () -> findFavoriteRecipesCount(memberId));
    }

    private List<Long> findFavoriteRecipeIds(Long memberId, int page) {
        long offset = (long) (page - 1) * myRecipeSize;
        return queryFactory
                .select(heart.recipe.id)
                .from(heart)
                .join(heart.recipe, recipe)
                .join(heart.member, member)
                .where(
                        member.id.eq(memberId)
                )
                .orderBy(heart.lastModifiedAt.desc())
                .offset(offset)
                .limit(myRecipeSize)
                .fetch();
    }

    private int findFavoriteRecipesCount(Long memberId) {
        Integer count = queryFactory
                .select(heart.id.count().intValue())
                .from(heart)
                .join(heart.member, member)
                .where(
                        member.id.eq(memberId)
                )
                .fetchOne();
        return count != null ? count : 0;
    }
    //endregion

}