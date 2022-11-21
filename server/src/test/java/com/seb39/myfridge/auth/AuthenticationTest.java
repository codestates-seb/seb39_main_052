package com.seb39.myfridge.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seb39.myfridge.domain.auth.domain.AuthenticationToken;
import com.seb39.myfridge.domain.auth.dto.LoginRequest;
import com.seb39.myfridge.domain.auth.dto.SignUpRequest;
import com.seb39.myfridge.domain.auth.enums.JwtTokenType;
import com.seb39.myfridge.domain.auth.repository.AuthenticationTokenRepository;
import com.seb39.myfridge.domain.auth.service.AuthenticationTokenProvider;
import com.seb39.myfridge.domain.auth.service.AuthenticationTokenService;
import com.seb39.myfridge.domain.auth.util.CookieUtils;
import com.seb39.myfridge.domain.member.entity.Member;
import com.seb39.myfridge.domain.member.service.MemberService;
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

import static com.seb39.myfridge.domain.auth.util.AppAuthNames.ACCESS_TOKEN;
import static com.seb39.myfridge.domain.auth.util.AppAuthNames.REFRESH_TOKEN;
import static com.seb39.myfridge.domain.auth.util.JwtClaims.ID;
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
                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("로그인한 사용자의 ID")
                )
        ));
    }

    @Test
    @DisplayName("게스트 로그인시 임의의 정보로 회원가입 후 일반 로그인과 동일하게 처리한다.")
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
                        headerWithName(ACCESS_TOKEN).description("Access Token이 담긴 헤더. (Refresh token은 refresh-token 쿠키에 담아 전송)")
                ),
                responseFields(
                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("로그인한 사용자의 ID")
                )
        ));
    }

    @Test
    @DisplayName("로그아웃시 서버에서 보관중인 Refresh token을 삭제한다")
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
                        headerWithName(AUTHORIZATION).description("Access token. (만료 여부 상관 없음), Refresh Token은 refresh-token Cookie에 담아 전송")
                )
        ));
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
    @DisplayName("구글 로그인 요청시 구글 로그인 페이지로 Redirect")
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