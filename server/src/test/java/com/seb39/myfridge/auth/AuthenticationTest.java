package com.seb39.myfridge.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seb39.myfridge.auth.dto.LoginRequest;
import com.seb39.myfridge.auth.dto.SignUpRequest;
import com.seb39.myfridge.auth.enums.JwtTokenType;
import com.seb39.myfridge.auth.service.JwtProvider;
import com.seb39.myfridge.auth.service.JwtService;
import com.seb39.myfridge.auth.util.CookieUtil;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;

import java.util.Date;

import static com.seb39.myfridge.auth.util.JwtClaims.*;
import static com.seb39.myfridge.auth.util.AppAuthNames.*;
import static com.seb39.myfridge.util.ApiDocumentUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriHost = "seb39myfridge.ml", uriScheme = "https" , uriPort = 443)
class AuthenticationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    MemberService memberService;
    @Autowired
    JwtService jwtService;
    @Autowired
    JwtProvider jwtProvider;
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
                .andExpect(header().exists(ACCESS_TOKEN))
                .andExpect(cookie().exists(REFRESH_TOKEN));

        // docs
        result.andDo(document("login-success",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                ),
                responseHeaders(
                        headerWithName(ACCESS_TOKEN).description("Access Token이 담긴 헤더. (Refresh token은 refresh-token 쿠키에 담아 전송)")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("실패시 실패 사유 Code (성공시 0)"),
                        fieldWithPath("failureReason").type(JsonFieldType.STRING).description("실패 사유 (성공시 공백)")
                )
        ));
    }

    @Test
    @DisplayName("로그아웃시 서버에서 보관중인 Refresh token을 삭제한다")
    void logout() throws Exception{
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

        String accessToken = jwtProvider.createAccessToken(member.getId());
        String refreshToken = jwtProvider.createRefreshToken(accessToken);
        Cookie refreshTokenCookie = CookieUtil.createHttpOnlyCookie(REFRESH_TOKEN, refreshToken);

        // expected
        ResultActions result = mockMvc.perform(get("/api/logout")
                        .accept(APPLICATION_JSON)
                        .header(ACCESS_TOKEN, accessToken)
                        .cookie(refreshTokenCookie))
                .andExpect(status().is3xxRedirection());

        assertThat(jwtService.hasRefreshToken(refreshToken)).isFalse();

        // docs
        result.andDo(document("logout-success",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                requestHeaders(
                        headerWithName(ACCESS_TOKEN).description("Access token. (만료 여부 상관 없음), Refresh Token은 refresh-token Cookie에 담아 전송")
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
                        .header(AUTHORIZATION, "Bearer " + expiredToken))
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
        String refreshToken = jwtProvider.createRefreshToken(expiredAccessToken);
        ReflectionTestUtils.invokeMethod(jwtService,"saveToken",refreshToken,expiredAccessToken);
        Cookie refreshTokenCookie = CookieUtil.createHttpOnlyCookie(REFRESH_TOKEN,refreshToken);

        // expected
        ResultActions result = mockMvc.perform(post("/api/auth/refresh")
                        .accept(APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer " + expiredAccessToken)
                        .cookie(refreshTokenCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(header().string(ACCESS_TOKEN, Matchers.not(expiredAccessToken)));

        // docs
        result.andDo(document("refresh-access-token",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                requestHeaders(
                        headerWithName(AUTHORIZATION).description("Access token. (만료 여부 상관 없음), Refresh Token은 refresh-token Cookie에 담아 전송")
                ),
                responseHeaders(
                        headerWithName(ACCESS_TOKEN).description("새로 생성한 Access Token")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("실패시 실패 사유 Code (성공시 0)"),
                        fieldWithPath("failureReason").type(JsonFieldType.STRING).description("실패 사유 (성공시 공백)")
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
        Cookie refreshTokenCookie = CookieUtil.createHttpOnlyCookie(REFRESH_TOKEN,expiredRefreshToken);


        // expected
        ResultActions result = mockMvc.perform(post("/api/auth/refresh")
                        .accept(APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer " +expiredAccessToken)
                        .cookie(refreshTokenCookie))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(4));
    }


    @Test
    @DisplayName("구글 로그인 요청시 구글 로그인 페이지로 Redirect")
    void googleOauth2Test() throws Exception {
        // given
        String redirectUriPrefix = "https://accounts.google.com/o/oauth2/v2/auth";
        // when
        mockMvc.perform(get("/oauth2/authorization/google"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location",Matchers.startsWith(redirectUriPrefix)))
                .andDo(print());
    }

    private String createExpiredAccessToken(Long id, String email) {
        return JWT.create()
                .withSubject(JwtTokenType.ACCESS.getSubject())
                .withClaim(ID, id)
                //.withClaim(EMAIL, email)
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