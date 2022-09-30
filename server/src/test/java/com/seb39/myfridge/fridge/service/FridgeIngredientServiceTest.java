package com.seb39.myfridge.fridge.service;

import com.seb39.myfridge.fridge.dto.FridgeDto;
import com.seb39.myfridge.fridge.entity.Fridge;
import com.seb39.myfridge.fridge.entity.FridgeIngredient;
import com.seb39.myfridge.fridge.mapper.FridgeMapper;
import com.seb39.myfridge.fridge.repository.FridgeIngredientRepository;
import com.seb39.myfridge.ingredient.Repository.IngredientRepository;
import com.seb39.myfridge.ingredient.entity.Ingredient;
import com.seb39.myfridge.ingredient.service.IngredientService;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FridgeIngredientServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private FridgeService fridgeService;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private FridgeIngredientRepository fridgeIngredientRepository;

    @Autowired
    private FridgeIngredientService fridgeIngredientService;

    @Autowired
    private FridgeMapper fridgeMapper;

    @Test
    public void 냉장고재료생성_테스트() {
        //given
        Member member = Member.generalBuilder()
                .email("test2@naver.com")
                .name("testA")
                .password("1234")
                .buildGeneralMember();

        memberService.signUpGeneral(member);

        Fridge fridge = fridgeService.findFridge(member.getId());

        Ingredient ingredient = new Ingredient();
        ingredient.setName("돼지고기");
        ingredientRepository.save(ingredient);

        FridgeIngredient fridgeIngredient = new FridgeIngredient();
        fridgeIngredient.setIngredient(ingredient);
        fridgeIngredient.setNote("국거리용");
        fridgeIngredient.setQuantity("300g");
        fridgeIngredient.setExpiration(LocalDate.of(2022, 10, 2));
        fridgeIngredient.setFridge(fridge);

        List<FridgeDto.IngredientInfo> ingredientInfos = new ArrayList<>();
        FridgeDto.IngredientInfo ingredientInfo= FridgeDto.IngredientInfo.builder()
                .expiration(fridgeIngredient.getExpiration())
                .quantity(fridgeIngredient.getQuantity())
                .note(fridgeIngredient.getNote())
                .name(fridgeIngredient.getIngredient().getName())
                .build();
        ingredientInfos.add(ingredientInfo);
        FridgeDto.Post requestBody = FridgeDto.Post.builder()
                .fridgeIngredients(ingredientInfos).build();
        //when
        List<FridgeIngredient> fridgeIngredients = fridgeIngredientService.createFridgeIngredient(fridge, requestBody);
        //then
        assertEquals(fridgeIngredients.get(0).getFridge().getMember().getId(), member.getId());
        assertEquals(fridgeIngredients.get(0).getIngredient().getName(), ingredient.getName());

    }

    @Test
    public void 냉장고비우기_테스트() {
        //given
        Member member = Member.generalBuilder()
                .email("test1@naver.com")
                .name("testA")
                .password("1234")
                .buildGeneralMember();

        memberService.signUpGeneral(member);

        Fridge fridge = fridgeService.findFridge(member.getId());
        List<FridgeDto.IngredientInfo> ingredientInfos = new ArrayList<>();

        Ingredient ingredient = new Ingredient();
        ingredient.setName("돼지고기");
        ingredientRepository.save(ingredient);

        FridgeIngredient fridgeIngredient = new FridgeIngredient();
        fridgeIngredient.setIngredient(ingredient);
        fridgeIngredient.setNote("국거리용");
        fridgeIngredient.setQuantity("300g");
        fridgeIngredient.setExpiration(LocalDate.of(2022, 10, 2));
        fridgeIngredient.setFridge(fridge);

        FridgeDto.IngredientInfo ingredientInfo = fridgeMapper.fridgeIngredientToFridgeIngredientDto(fridgeIngredient);
        ingredientInfos.add(ingredientInfo);

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("우유");
        ingredientRepository.save(ingredient1);

        FridgeIngredient fridgeIngredient1 = new FridgeIngredient();
        fridgeIngredient1.setIngredient(ingredient1);
        fridgeIngredient1.setNote("서울우유");
        fridgeIngredient1.setQuantity("1L");
        fridgeIngredient1.setExpiration(LocalDate.of(2022, 10, 5));
        fridgeIngredient1.setFridge(fridge);

        FridgeDto.IngredientInfo ingredientInfo1 = fridgeMapper.fridgeIngredientToFridgeIngredientDto(fridgeIngredient1);
        ingredientInfos.add(ingredientInfo1);
        FridgeDto.Post requestBody = FridgeDto.Post.builder()
                .fridgeIngredients(ingredientInfos).build();

        //when
        fridgeIngredientService.deleteFridgeIngredient(fridge.getId());
        //then
        List<FridgeIngredient> ingredientList = fridgeIngredientService.findFridgeIngredient(fridge.getId());
        assertEquals(ingredientList.size(), 0);

    }

//    @Test
    public void 냉장고재료찾기_테스트() {
        //given
        Member member = Member.generalBuilder()
                .email("test3@naver.com")
                .name("testA")
                .password("1234")
                .buildGeneralMember();

        memberService.signUpGeneral(member);

        Fridge fridge = fridgeService.findFridge(member.getId());
        List<FridgeDto.IngredientInfo> ingredientInfos = new ArrayList<>();

        Ingredient ingredient = new Ingredient();
        ingredient.setName("돼지고기");
        ingredientRepository.save(ingredient);

        FridgeIngredient fridgeIngredient = new FridgeIngredient();
        fridgeIngredient.setIngredient(ingredient);
        fridgeIngredient.setNote("국거리용");
        fridgeIngredient.setQuantity("300g");
        fridgeIngredient.setExpiration(LocalDate.of(2022, 10, 2));
        fridgeIngredient.setFridge(fridge);

        FridgeDto.IngredientInfo ingredientInfo = fridgeMapper.fridgeIngredientToFridgeIngredientDto(fridgeIngredient);
        ingredientInfos.add(ingredientInfo);

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("우유");
        ingredientRepository.save(ingredient1);

        FridgeIngredient fridgeIngredient1 = new FridgeIngredient();
        fridgeIngredient1.setIngredient(ingredient1);
        fridgeIngredient1.setNote("서울우유");
        fridgeIngredient1.setQuantity("700ml");
        fridgeIngredient1.setExpiration(LocalDate.of(2022, 10, 5));
        fridgeIngredient1.setFridge(fridge);

        FridgeDto.IngredientInfo ingredientInfo1 = fridgeMapper.fridgeIngredientToFridgeIngredientDto(fridgeIngredient1);
        ingredientInfos.add(ingredientInfo1);
        FridgeDto.Post requestBody = FridgeDto.Post.builder()
                .fridgeIngredients(ingredientInfos).build();


        fridgeIngredientService.createFridgeIngredient(fridge, requestBody);
        //when
        List<FridgeIngredient> ingredients = fridgeIngredientService.findFridgeIngredient(fridge.getId());
        FridgeDto.Response response = fridgeMapper.fridgeIngredientsToResponse(ingredients);
        //then
        List<FridgeDto.IngredientInfoResponse> responses = response.getFridgeIngredients();
        assertEquals(responses.get(0).getName(), fridgeIngredient.getIngredient().getName());
        assertEquals(responses.get(0).getNote(), fridgeIngredient.getNote());
        assertEquals(responses.get(0).getQuantity(), fridgeIngredient.getQuantity());
        assertEquals(responses.get(0).getExpiration(), fridgeIngredient.getExpiration());

        assertEquals(responses.get(1).getName(), fridgeIngredient1.getIngredient().getName());
        assertEquals(responses.get(1).getNote(), fridgeIngredient1.getNote());
        assertEquals(responses.get(1).getQuantity(), fridgeIngredient1.getQuantity());
        assertEquals(responses.get(1).getExpiration(), fridgeIngredient1.getExpiration());
    }
}