package com.seb39.myfridge.member.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    void commonBuilderTest(){
        Member m = Member.commonBuilder()
                .name("common")
                .password("1234")
                .email("dog@cat.com")
                .buildCommonMember();

        assertThat(m.getName()).isEqualTo("common");
        assertThat(m.getPassword()).isEqualTo("1234");
        assertThat(m.getEmail()).isEqualTo("dog@cat.com");
    }

    @Test
    void oauth2BuilderTest(){
        Member m = Member.oauth2Builder()
                .name("oauth2")
                .provider("KAKAO")
                .providerId("123456789")
                .buildOAuth2Member();

        assertThat(m.getName()).isEqualTo("oauth2");
        assertThat(m.getProvider()).isEqualTo("KAKAO");
        assertThat(m.getProviderId()).isEqualTo("123456789");
    }
}