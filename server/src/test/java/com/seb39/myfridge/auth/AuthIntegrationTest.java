package com.seb39.myfridge.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seb39.myfridge.auth.dto.LoginRequest;
import com.seb39.myfridge.auth.dto.SignUpRequest;
import com.seb39.myfridge.auth.enums.AuthCookieType;
import com.seb39.myfridge.auth.service.JwtService;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.service.MemberService;
import com.seb39.myfridge.util.ApiDocumentUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.seb39.myfridge.util.ApiDocumentUtils.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriHost = "api.myfridge.com")
class AuthIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    MemberService memberService;
    @Autowired
    JwtService jwtService;

    private ObjectMapper om = new ObjectMapper();

    @Test
    @DisplayName("회원가입 성공")
    void signUpSuccessTest() throws Exception {
        // given
        String email = "user01@gmail.com";
        String password = "abcdefg123";
        String name = "test";

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail(email);
        signUpRequest.setPassword(password);
        signUpRequest.setName(name);

        String requestBody = om.writeValueAsString(signUpRequest);

        // expected
        ResultActions result = mockMvc.perform(post("/api/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));

        // docs
        result.andDo(document("signup-success",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름(닉네임)")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                        fieldWithPath("failureReason").type(JsonFieldType.STRING).description("실패 사유 (성공시 공백)")
                )
        ));
    }

    @Test
    @DisplayName("로그인 성공시 서버에서 access token 및 refresh token을 발급한다.")
    void loginSuccessTest() throws Exception {
        // given
        String email = "abcd@gmail.com";
        String name = "test01";
        String password = "password1234";
        Member member = Member.generalBuilder()
                .email(email)
                .name(name)
                .password(password)
                .buildGeneralMember();
        memberService.signUpGeneral(member);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);
        String requestBody = om.writeValueAsString(loginRequest);

        // expected
        ResultActions result = mockMvc.perform(post("/api/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andExpect(cookie().exists(AuthCookieType.REFRESH_TOKEN.getName()));

        // docs
//        result.andDo(document("login-success",
//                getRequestPreProcessor(),
//                getResponsePreProcessor(),
//
//
//
//                ))

    }

    @Test
    @DisplayName("존재하지 않는 사용자로 로그인시 401로 응답한다.")
    void notExistMemberLogin(){

    }


    @Test
    @DisplayName("비밀번호가 다른 경우 401로 응답한다.")
    void invalidPasswordLogin(){

    }

//    @Test
//    @DisplayName("Access Token이 만료된 경우 재발급한다?")
    // Front-end 확인 필요

}