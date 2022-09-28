package com.seb39.myfridge.fridge.service;

import com.seb39.myfridge.fridge.entity.Fridge;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FridgeServiceTest {


    @Autowired
    private FridgeService fridgeService;

    @Autowired
    private MemberService memberService;


    @Test
    public void 냉장고생성_테스트_일반회원() {
        //given
        Member member = Member.generalBuilder()
                .email("test@naver.com")
                .name("testA")
                .password("1234")
                .buildGeneralMember();

        memberService.signUpGeneral(member);
        //when
        Fridge fridge = fridgeService.createFridge(member);
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
        Fridge fridge = fridgeService.createFridge(auth2Member);
        //then
        assertEquals(fridge.getMember().getId(), auth2Member.getId());
        assertEquals(fridge.getMember().getEmail(), auth2Member.getEmail());
        assertEquals(fridge.getMember().getPassword(), auth2Member.getPassword());
        assertEquals(fridge.getMember().getProvider(), auth2Member.getProvider());
        assertEquals(fridge.getMember().getProfileImagePath(), auth2Member.getProfileImagePath());
    }
}