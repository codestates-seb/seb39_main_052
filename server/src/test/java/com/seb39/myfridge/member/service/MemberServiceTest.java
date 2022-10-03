package com.seb39.myfridge.member.service;

import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.config.web.servlet.oauth2.client.OAuth2ClientSecurityMarker;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.FileInputStream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    void existOAuth2MemberTest() throws Exception {
        // given
        String provider = "google";
        String providerId = "123123123";
        Member member = Member.oauth2Builder()
                .name("SJ")
                .email("sj@gmail.com")
                .provider(provider)
                .providerId(providerId)
                .buildOAuth2Member();
        memberService.signUpOauth2IfNotExists(member);

        // when
        boolean exist = memberService.existOAuth2Member(provider, providerId);

        // then
        assertThat(exist).isTrue();
    }

    @Test
    void findOAuth2MemberTest() throws Exception {
        // given
        String provider = "kakao";
        String providerId = "111112222233333";
        Member member = Member.oauth2Builder()
                .name("SJ")
                .email("sj@gmail.com")
                .provider(provider)
                .providerId(providerId)
                .buildOAuth2Member();
        memberService.signUpOauth2IfNotExists(member);

        // when
        Member findMember = memberService.findOAuth2Member(provider, providerId);

        // then
        assertThat(findMember.getName()).isEqualTo("SJ");
        assertThat(findMember.getProvider()).isEqualTo(provider);
        assertThat(findMember.getProviderId()).isEqualTo(providerId);
    }

    @Test
    @DisplayName("회원의 이름을 업데이트 한다.")
    void updateNameTest() throws Exception {
        // given
        Member member = Member.generalBuilder()
                .name("member")
                .password("12345")
                .buildGeneralMember();
        memberService.signUpGeneral(member);

        // when
        String newName = "NEW-Member";
        memberService.updateName(member, newName);

        // then
        assertThat(member.getName()).isEqualTo(newName);
    }

    @Test
    @DisplayName("회원의 프로필 사진을 업데이트 한다.")
    void updateProfileImageTest() throws Exception {
        // given
        Member member = Member.generalBuilder()
                .name("member")
                .password("12345")
                .buildGeneralMember();
        memberService.signUpGeneral(member);

        MockMultipartFile image = new MockMultipartFile("profileImage", "프로필사진.jpg", "image/png", new FileInputStream("src/test/resources/image/puppy.jpeg"));

        // when
        memberService.updateProfileImage(member, image);

        // then
        assertThat(member.getProfileImagePath()).isNotNull();
    }
}