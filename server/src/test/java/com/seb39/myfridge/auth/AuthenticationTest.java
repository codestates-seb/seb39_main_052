package com.seb39.myfridge.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seb39.myfridge.auth.dto.LoginRequest;
import com.seb39.myfridge.auth.dto.SignUpRequest;
import com.seb39.myfridge.auth.enums.AuthCookieType;
import com.seb39.myfridge.auth.enums.JwtClaim;
import com.seb39.myfridge.auth.enums.JwtTokenType;
import com.seb39.myfridge.auth.service.JwtService;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.service.MemberService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;

import java.util.Date;

import static com.seb39.myfridge.util.ApiDocumentUtils.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriHost = "api.myfridge.com")
class AuthenticationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    MemberService memberService;
    @Autowired
    JwtService jwtService;
    @Value("${app.auth.jwt.secret}")
    private String secret;

    private ObjectMapper om = new ObjectMapper();

    @Test
    @DisplayName("회원가입 성공")
    void signUpSuccess() throws Exception {
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
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
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
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("실패시 실패 사유 Code (성공시 0)"),
                        fieldWithPath("failureReason").type(JsonFieldType.STRING).description("실패 사유 (성공시 공백)")
                )
        ));
    }

    @Test
    @DisplayName("로그인 성공시 서버에서 access token 및 refresh token을 발급한다.")
    void loginSuccess() throws Exception {
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
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(header().exists(AUTHORIZATION))
                .andExpect(cookie().exists(AuthCookieType.REFRESH_TOKEN.getName()));

        // docs
        result.andDo(document("login-success",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                ),
                responseHeaders(
                        headerWithName(AUTHORIZATION).description("Access Token이 담긴 헤더. (Refresh token은 refresh-token 쿠키에 담아 전송)")
                )
        ));
    }

    @Test
    @DisplayName("존재하지 않는 사용자로 로그인시 401로 응답한다.")
    void notExistMemberLogin() throws Exception {

        // given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("notexist@naver.com");
        loginRequest.setPassword("abcdefg1234");
        String requestBody = om.writeValueAsString(loginRequest);

        // expected
        ResultActions result = mockMvc.perform(post("/api/login")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @DisplayName("비밀번호가 다른 경우 401로 응답한다.")
    void invalidPasswordLogin() throws Exception {
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
        loginRequest.setPassword(password + "111");
        String requestBody = om.writeValueAsString(loginRequest);

        // expected
        ResultActions result = mockMvc.perform(post("/api/login")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("USER 권한이 필요한 주소로 요청시 Access Token이 만료된 경우 Http Status 401, Body의 Code를 1로 응답한다.")
    void expiredAccessToken() throws Exception {
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

        String expiredToken = createExpiredAccessToken(member.getId(), member.getEmail());

        // expected
        ResultActions result = mockMvc.perform(get("/api/authtest")
                        .accept(APPLICATION_JSON)
                        .header(AUTHORIZATION, jwtService.accessTokenToAuthorizationHeader(expiredToken)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(1));
    }

    @Test
    @DisplayName("Access token 재발급 성공")
    void accessTokenRefresh() throws Exception {
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
        String expiredAccessToken = createExpiredAccessToken(member.getId(), member.getEmail());
        String refreshToken = jwtService.createRefreshToken(expiredAccessToken);
        Cookie refreshTokenCookie = jwtService.refreshTokenToCookie(refreshToken);

        // expected
        ResultActions result = mockMvc.perform(post("/api/auth/refresh")
                        .accept(APPLICATION_JSON)
                        .header(AUTHORIZATION, jwtService.accessTokenToAuthorizationHeader(expiredAccessToken))
                        .cookie(refreshTokenCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(header().string(AUTHORIZATION, Matchers.not(expiredAccessToken)));

        // docs
        result.andDo(document("refresh-access-token",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                requestHeaders(
                        headerWithName(AUTHORIZATION).description("Access token. (만료 여부 상관 없음), Refresh Token은 refresh-token Cookie에 담아 전송")
                ),
                responseHeaders(
                        headerWithName(AUTHORIZATION).description("새로 생성한 Access Token")
                )
        ));
    }

    @Test
    @DisplayName("Refresh token 재발급 요청시 Refresh Token이 만료된 경우 Http Status 401, Body의 Code를 4로 응답한다.")
    void expiredRefreshToken() throws Exception {
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
        String expiredAccessToken = createExpiredAccessToken(member.getId(), member.getEmail());
        String expiredRefreshToken = createExpiredRefreshToken();
        ReflectionTestUtils.invokeMethod(jwtService,"saveToken",expiredRefreshToken,expiredAccessToken);
        Cookie refreshTokenCookie = jwtService.refreshTokenToCookie(expiredRefreshToken);


        // expected
        ResultActions result = mockMvc.perform(post("/api/auth/refresh")
                        .accept(APPLICATION_JSON)
                        .header(AUTHORIZATION, jwtService.accessTokenToAuthorizationHeader(expiredAccessToken))
                        .cookie(refreshTokenCookie))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(4));
    }

    private String createExpiredAccessToken(Long id, String email) {
        return JWT.create()
                .withSubject(JwtTokenType.ACCESS.getSubject())
                .withClaim(JwtClaim.ID.toString(), id)
                .withClaim(JwtClaim.EMAIL.toString(), email)
                .withExpiresAt(new Date(System.currentTimeMillis() - 10000))
                .sign(Algorithm.HMAC512(secret));
    }

    private String createExpiredRefreshToken() {
        return JWT.create()
                .withSubject(JwtTokenType.REFRESH.getSubject())
                .withExpiresAt(new Date(System.currentTimeMillis() - 10000))
                .sign(Algorithm.HMAC512(secret));
    }
}