package com.seb39.myfridge.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seb39.myfridge.auth.domain.AuthenticationToken;
import com.seb39.myfridge.auth.dto.LoginRequest;
import com.seb39.myfridge.auth.enums.AppAuthExceptionCode;
import com.seb39.myfridge.auth.enums.JwtTokenType;
import com.seb39.myfridge.auth.service.AuthenticationTokenProvider;
import com.seb39.myfridge.auth.service.AuthenticationTokenService;
import com.seb39.myfridge.auth.util.CookieUtils;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;

import java.util.Date;

import static com.seb39.myfridge.auth.util.AppAuthNames.REFRESH_TOKEN;
import static com.seb39.myfridge.auth.util.JwtClaims.ID;
import static com.seb39.myfridge.util.ApiDocumentUtils.getRequestPreProcessor;
import static com.seb39.myfridge.util.ApiDocumentUtils.getResponsePreProcessor;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriHost = "seb39myfridge.ml", uriScheme = "https" , uriPort = 443)
public class AuthenticationFailureTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    MemberService memberService;
    @Autowired
    AuthenticationTokenService authenticationTokenService;
    @Autowired
    AuthenticationTokenProvider jwtProvider;
    @Value("${app.auth.jwt.secret}")
    private String secret;

    private ObjectMapper om = new ObjectMapper();

    @Test
    @DisplayName("존재하지 않는 사용자로 로그인시 status 401, INVALID_EMAIL_OR_PASSWORD로 응답한다.")
    void notExistMemberLogin() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("notexist@naver.com");
        loginRequest.setPassword("abcdefg1234");
        String requestBody = om.writeValueAsString(loginRequest);

        // expected
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/login")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(AppAuthExceptionCode.INVALID_EMAIL_OR_PASSWORD.getCode()));

        result.andDo(document("login-fail",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("로그인 성공 여부"),
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("인증 실패 Error Code"),
                        fieldWithPath("failureReason").type(JsonFieldType.STRING).description("실패 사유")
                )
        ));
    }

    @Test
    @DisplayName("비밀번호가 다른 경우 status 401, INVALID_EMAIL_OR_PASSWORD 로 응답한다.")
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
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(AppAuthExceptionCode.INVALID_EMAIL_OR_PASSWORD.getCode()));
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

        String expiredToken = createExpiredAccessToken(member.getId());

        // expected
        ResultActions result = mockMvc.perform(get("/api/authtest")
                        .accept(APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer " + expiredToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(1));
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
        String expiredAccessToken = createExpiredAccessToken(member.getId());
        String expiredRefreshToken = createExpiredRefreshToken();
        AuthenticationToken token = new AuthenticationToken(expiredAccessToken, expiredRefreshToken);
        ReflectionTestUtils.invokeMethod(authenticationTokenService,"saveToken",token);
        Cookie refreshTokenCookie = CookieUtils.createHttpOnlyCookie(REFRESH_TOKEN,expiredRefreshToken);


        // expected
        ResultActions result = mockMvc.perform(post("/api/auth/refresh")
                        .accept(APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer " +expiredAccessToken)
                        .cookie(refreshTokenCookie))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(4));
    }

    private String createExpiredAccessToken(Long id) {
        return JWT.create()
                .withSubject(JwtTokenType.ACCESS.getSubject())
                .withClaim(ID, id)
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
