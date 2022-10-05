package com.seb39.myfridge.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seb39.myfridge.comment.dto.CommentDto;
import com.seb39.myfridge.comment.entity.Comment;
import com.seb39.myfridge.member.dto.MemberDto;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.repository.MemberRepository;
import com.seb39.myfridge.member.service.MemberService;
import com.seb39.myfridge.recipe.entity.Recipe;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.server.Encoding;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import javax.sound.sampled.AudioFormat;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.seb39.myfridge.util.ApiDocumentUtils.getRequestPreProcessor;
import static com.seb39.myfridge.util.ApiDocumentUtils.getResponsePreProcessor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
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

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ObjectMapper om;

    @BeforeEach
    void beforeEach() {
        Member member = Member.generalBuilder()
                .name("USER01")
                .password("abcdefgh")
                .email("test@gmail.com")
                .buildGeneralMember();
        memberService.signUpGeneral(member);
    }

    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("사용자 정보를 응답한다")
    void getMember() throws Exception {
        Member member = memberRepository.findAll().get(0);

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
                        fieldWithPath("profileImagePath").type(JsonFieldType.STRING).description("조회된 사용자의 프로필 이미지 경로").optional()
                )
        ));
    }

    @Test
    @DisplayName("로그인한 사용자가 자신의 정보를 수정한다.")
    @WithUserDetails(value = "test@gmail.com", userDetailsServiceBeanName = "principalDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void patchMemberTest() throws Exception {
        // given
        Member member = memberRepository.findAll().get(0);
        String prevName = member.getName();
        String prevProfileImagePath = member.getProfileImagePath();

        MemberDto.Patch requestBody = new MemberDto.Patch();
        requestBody.setName("USER01-update");
        String requestJson = om.writeValueAsString(requestBody);

        MockMultipartFile json = new MockMultipartFile("requestBody", "jsonData", "application/json", requestJson.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile image = new MockMultipartFile("profileImage", "프로필사진.jpg", "image/png", new FileInputStream("src/test/resources/image/puppy.jpeg"));

        // expected
        ResultActions result = mockMvc.perform(multipart("/api/members")
                        .file(json)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .with((request) -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(jsonPath("$.id").value(member.getId()))
                .andExpect(jsonPath("$.name").value(requestBody.getName()))
                .andExpect(jsonPath("$.profileImagePath").value(Matchers.not(prevProfileImagePath)))
                .andDo(MockMvcResultHandlers.print());

        // docs
        result.andDo(document("member-update",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                requestPartBody("requestBody"),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("수정된 사용자 ID"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("수정된 사용자의 이름"),
                        fieldWithPath("profileImagePath").type(JsonFieldType.STRING).description("수정된 사용자의 프로필 이미지 경로")
                )
        ));
    }
}