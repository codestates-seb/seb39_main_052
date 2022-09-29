package com.seb39.myfridge.member.controller;

import com.seb39.myfridge.comment.dto.CommentDto;
import com.seb39.myfridge.comment.entity.Comment;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.service.MemberService;
import com.seb39.myfridge.recipe.entity.Recipe;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.seb39.myfridge.util.ApiDocumentUtils.getRequestPreProcessor;
import static com.seb39.myfridge.util.ApiDocumentUtils.getResponsePreProcessor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriHost = "seb39myfridge.ml", uriScheme = "https", uriPort = 443)
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("사용자 정보를 응답한다")
    void getMember() throws Exception {
        //given
        Member member = Member.oauth2Builder()
                .name("USER01")
                .email("user01@gmail.com")
                .provider("google")
                .providerId("111222333444")
                .profileImagePath("https://lh3.googleusercontent.com/a/ALm5wu0rHz0huDxsoYqO-TdP8efhVar7Lc7R0T9Jbvb3=s96-c")
                .buildOAuth2Member();
        memberService.signUpOauth2IfNotExists(member);

        //expected
        ResultActions result = mockMvc.perform(get("/api/members/{memberId}", member.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(member.getId()))
                .andExpect(jsonPath("$.name").value(member.getName()))
                .andExpect(jsonPath("$.profileImagePath").value(member.getProfileImagePath()));

        // docs
        result.andDo(document("member-read",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                pathParameters(
                        parameterWithName("memberId").description("조회할 사용자의 ID")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("조회된 작성자 ID"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("조회된 사용자의 이름"),
                        fieldWithPath("profileImagePath").type(JsonFieldType.STRING).description("조회된 사용자의 프로필 이미지 경로")
                )
        ));
    }
}