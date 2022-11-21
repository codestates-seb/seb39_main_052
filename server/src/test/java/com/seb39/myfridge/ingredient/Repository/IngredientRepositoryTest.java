package com.seb39.myfridge.ingredient.Repository;

import com.seb39.myfridge.domain.ingredient.Repository.IngredientRepository;
import com.seb39.myfridge.global.config.QueryDslConfig;
import com.seb39.myfridge.domain.ingredient.entity.Ingredient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@DataJpaTest
@Import(QueryDslConfig.class)
class IngredientRepositoryTest {

    @Autowired
    IngredientRepository ingredientRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("특정 문자열이 포함된 재료 이름 리스트 조회.")
    void searchNames() throws Exception {
        // given
        List<String> names = List.of("간장", "설탕", "고추장", "대파", "쪽파", "된장");
        for (String name : names) {
            Ingredient ingredient = new Ingredient();
            ingredient.setName(name);
            ingredientRepository.save(ingredient);
        }

        em.flush();
        em.clear();

        List<String> collect = ingredientRepository.findAll()
                .stream()
                .map(Ingredient::getName)
                .collect(Collectors.toList());
        System.out.println(collect);

        // when
        List<String> ingredientNames = ingredientRepository.searchNames("장");

        //then
        Assertions.assertThat(ingredientNames.size()).isEqualTo(3);
        Assertions.assertThat(ingredientNames)
                .anyMatch(title -> title.contains("장"));
    }
}