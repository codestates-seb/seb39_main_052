package com.seb39.myfridge.recipe.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seb39.myfridge.member.entity.QMember;
import com.seb39.myfridge.recipe.entity.QRecipe;
import lombok.RequiredArgsConstructor;
import org.hibernate.criterion.LikeExpression;

import java.util.List;

import static com.seb39.myfridge.recipe.entity.QRecipe.*;

@RequiredArgsConstructor
public class RecipeRepositoryImpl implements RecipeRepositoryCustom {
    private final JPAQueryFactory queryFactory;


    public List<String> searchTitles(String title) {
        return queryFactory
                .select(recipe.title)
                .from(recipe)
                .where(
                        recipe.title.upper().like("%"+title.toUpperCase()+"%")
                )
                .limit(10)
                .fetch();
    }
}
