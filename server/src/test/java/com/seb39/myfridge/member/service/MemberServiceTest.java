package com.seb39.myfridge.member.service;

import com.seb39.myfridge.member.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.web.servlet.oauth2.client.OAuth2ClientSecurityMarker;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@OAuth2ClientSecurityMarker
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    void existOAuth2MemberTest() throws Exception {
        // given
        String provider = "GOOGLE";
        String providerId = "123123123";
        Member member = Member.oauth2Builder()
                .name("SJ")
                .email("sj@gmail.com")
                .provider(provider)
                .providerId(providerId)
                .buildOAuth2Member();
        memberService.signUpOauth2(member);

        // when
        boolean exist = memberService.existOAuth2Member(provider, providerId);

        // then
        assertThat(exist).isTrue();
    }
}