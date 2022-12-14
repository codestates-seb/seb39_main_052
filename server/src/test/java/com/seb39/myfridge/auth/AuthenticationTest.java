package com.seb39.myfridge.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seb39.myfridge.auth.domain.AuthenticationToken;
import com.seb39.myfridge.auth.dto.LoginRequest;
import com.seb39.myfridge.auth.dto.SignUpRequest;
import com.seb39.myfridge.auth.enums.JwtTokenType;
import com.seb39.myfridge.auth.repository.AuthenticationTokenRepository;
import com.seb39.myfridge.auth.service.AuthenticationTokenProvider;
import com.seb39.myfridge.auth.service.AuthenticationTokenService;
import com.seb39.myfridge.auth.util.CookieUtils;
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
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriHost = "seb39myfridge.ml", uriScheme = "https", uriPort = 443)
class AuthenticationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    MemberService memberService;
    @Autowired
    AuthenticationTokenService tokenService;
    @Autowired
    AuthenticationTokenProvider jwtProvider;

    @Autowired
    AuthenticationTokenRepository tokenRepository;

    @Value("${app.auth.jwt.secret}")
    private String secret;

    private ObjectMapper om = new ObjectMapper();

    @Test
    @DisplayName("???????????? ??????")
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
                        fieldWithPath("email").type(JsonFieldType.STRING).description("????????? ?????????"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("????????????"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("????????? ??????(?????????)")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("?????? ??????"),
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("????????? ?????? ?????? Code (????????? 0)"),
                        fieldWithPath("failureReason").type(JsonFieldType.STRING).description("?????? ?????? (????????? ??????)")
                )
        ));
    }

    @Test
    @DisplayName("????????? ????????? ???????????? access token ??? refresh token??? ????????????.")
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
                        fieldWithPath("email").type(JsonFieldType.STRING).description("????????? ?????????"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("????????????")
                ),
                responseHeaders(
                        headerWithName(ACCESS_TOKEN).description("Access Token??? ?????? ??????. (Refresh token??? refresh-token ????????? ?????? ??????)")
                ),
                responseFields(
                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("???????????? ???????????? ID")
                )
        ));
    }

    @Test
    @DisplayName("????????? ???????????? ????????? ????????? ???????????? ??? ?????? ???????????? ???????????? ????????????.")
    void guestLogin() throws Exception {

        // expected
        ResultActions result = mockMvc.perform(post("/api/login/guest")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().exists(ACCESS_TOKEN))
                .andExpect(cookie().exists(REFRESH_TOKEN));

        // docs
        result.andDo(document("login-guest",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                responseHeaders(
                        headerWithName(ACCESS_TOKEN).description("Access Token??? ?????? ??????. (Refresh token??? refresh-token ????????? ?????? ??????)")
                ),
                responseFields(
                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("???????????? ???????????? ID")
                )
        ));
    }

    @Test
    @DisplayName("??????????????? ???????????? ???????????? Refresh token??? ????????????")
    void logout() throws Exception {
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

        AuthenticationToken token = jwtProvider.createToken(member.getId());
        String accessToken = token.getAccess();
        String refreshToken = token.getRefresh();
        Cookie refreshTokenCookie = CookieUtils.createHttpOnlyCookie(REFRESH_TOKEN, refreshToken);

        // expected
        ResultActions result = mockMvc.perform(get("/api/logout")
                        .accept(APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer " + accessToken)
                        .cookie(refreshTokenCookie))
                .andExpect(status().isOk());

        assertThat(tokenRepository.findById(refreshToken)).isEmpty();

        // docs
        result.andDo(document("logout-success",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                requestHeaders(
                        headerWithName(AUTHORIZATION).description("Access token. (?????? ?????? ?????? ??????), Refresh Token??? refresh-token Cookie??? ?????? ??????")
                )
        ));
    }

    @Test
    @DisplayName("Access token ????????? ??????")
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

        AuthenticationToken token = jwtProvider.createToken(member.getId());
        String expiredAccessToken = createExpiredAccessToken(member.getId());
        ReflectionTestUtils.setField(token,"access",expiredAccessToken);

        ReflectionTestUtils.invokeMethod(tokenService, "saveToken", token);
        Cookie refreshTokenCookie = CookieUtils.createHttpOnlyCookie(REFRESH_TOKEN, token.getRefresh());

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
                        headerWithName(AUTHORIZATION).description("Access token. (?????? ?????? ?????? ??????), Refresh Token??? refresh-token Cookie??? ?????? ??????")
                ),
                responseHeaders(
                        headerWithName(ACCESS_TOKEN).description("?????? ????????? Access Token")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("?????? ??????"),
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("????????? ?????? ?????? Code (????????? 0)"),
                        fieldWithPath("failureReason").type(JsonFieldType.STRING).description("?????? ?????? (????????? ??????)")
                )
        ));
    }

    @Test
    @DisplayName("?????? ????????? ????????? ?????? ????????? ???????????? Redirect")
    void googleOauth2Test() throws Exception {
        // given
        String redirectUriPrefix = "https://accounts.google.com/o/oauth2/v2/auth";
        // when
        mockMvc.perform(get("/oauth2/authorization/google"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", Matchers.startsWith(redirectUriPrefix)))
                .andDo(print());
    }

    private String createExpiredAccessToken(Long id) {
        return JWT.create()
                .withSubject(JwtTokenType.ACCESS.getSubject())
                .withClaim(ID, id)
                .withExpiresAt(new Date(System.currentTimeMillis() - 10000))
                .sign(Algorithm.HMAC512(secret));
    }
}