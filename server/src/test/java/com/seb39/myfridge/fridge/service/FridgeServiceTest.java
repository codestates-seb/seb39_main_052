package com.seb39.myfridge.fridge.service;

import com.seb39.myfridge.fridge.dto.FridgeDto;
import com.seb39.myfridge.fridge.entity.Fridge;
import com.seb39.myfridge.fridge.entity.FridgeIngredient;
import com.seb39.myfridge.fridge.mapper.FridgeMapper;
import com.seb39.myfridge.ingredient.Repository.IngredientRepository;
import com.seb39.myfridge.ingredient.entity.Ingredient;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FridgeServiceTest {

    @Autowired
    private FridgeService fridgeService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private FridgeIngredientService fridgeIngredientService;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private FridgeMapper fridgeMapper;


    @Test
    public void 냉장고생성_테스트_일반회원() {
        //given
        Member member = Member.generalBuilder()
                .email("test10@naver.com")
                .name("testB")
                .password("1234")
                .buildGeneralMember();

        memberService.signUpGeneral(member);
        //when
        Fridge fridge = fridgeService.findFridge(member.getId());
        //then
        assertEquals(fridge.getMember().getEmail(), member.getEmail());
        assertEquals(fridge.getMember().getId(), member.getId());
        assertEquals(fridge.getMember().getPassword(), member.getPassword());
    }

    @Test
    public void 냉장고생성_테스트_소셜회원() {
        //given
        Member auth2Member = Member.oauth2Builder()
                .name("oauthTest")
                .email("oauth@naver.com")
                .provider("Google")
                .profileImagePath("imagePath")
                .providerId("providerId")
                .buildOAuth2Member();

        memberService.signUpOauth2IfNotExists(auth2Member);
        //when
        Fridge fridge = fridgeService.findFridge(auth2Member.getId());
        //then
        assertEquals(fridge.getMember().getId(), auth2Member.getId());
        assertEquals(fridge.getMember().getEmail(), auth2Member.getEmail());
        assertEquals(fridge.getMember().getPassword(), auth2Member.getPassword());
        assertEquals(fridge.getMember().getProvider(), auth2Member.getProvider());
        assertEquals(fridge.getMember().getProfileImagePath(), auth2Member.getProfileImagePath());
    }



}