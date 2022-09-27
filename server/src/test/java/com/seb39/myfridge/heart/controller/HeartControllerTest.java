package com.seb39.myfridge.heart.controller;

import com.seb39.myfridge.heart.service.HeartService;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.repository.MemberRepository;
import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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


import static com.seb39.myfridge.util.ApiDocumentUtils.getRequestPreProcessor;
import static com.seb39.myfridge.util.ApiDocumentUtils.getResponsePreProcessor;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
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
@WithUserDetails(value = "test@gmail.com", userDetailsServiceBeanName = "principalDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
class HeartControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    HeartService heartService;

    @BeforeEach
    void beforeEach() {
        Member member = Member.generalBuilder()
                .email("test@gmail.com")
                .buildGeneralMember();
        memberRepository.save(member);

        Recipe recipe = new Recipe();
        recipe.setMember(member);
        recipeRepository.save(recipe);
    }

    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
    }


    @Test
    @DisplayName("레시피에 하트를 추가한다")
    void postHeart() throws Exception {
        //given
        Member member = memberRepository.findAll().get(0);
        Recipe recipe = recipeRepository.findAll().get(0);

        int prevHeartCounts = 10;
        for (int i = 0; i < prevHeartCounts; i++) {
            Member newMember = Member.generalBuilder()
                    .buildGeneralMember();
            memberRepository.save(newMember);
            heartService.addHeartToRecipe(newMember.getId(), recipe.getId());
        }

        //expected
        ResultActions result = mockMvc.perform(post("/api/recipes/{recipeId}/heart", recipe.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipeId").value(recipe.getId()))
                .andExpect(jsonPath("$.heartCounts").value(prevHeartCounts + 1));

        // docs
        result.andDo(document("heart-create",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                pathParameters(
                        parameterWithName("recipeId").description("하트를 추가할 레시피 ID")
                ),
                responseFields(
                        fieldWithPath("recipeId").type(JsonFieldType.NUMBER).description("하트가 추가된 레시피 ID"),
                        fieldWithPath("heartCounts").type(JsonFieldType.NUMBER).description("레시피에 달린 총 하트 개수")
                )
        ));
    }

    @Test
    @DisplayName("레시피에 자신이 추가한 하트를 제거한다")
    void deleteHeart() throws Exception {
        //given
        Member member = memberRepository.findAll().get(0);
        Recipe recipe = recipeRepository.findAll().get(0);

        int prevHeartCounts = 10;
        heartService.addHeartToRecipe(member.getId(),recipe.getId());
        for (int i = 0; i < prevHeartCounts-1; i++) {
            Member newMember = Member.generalBuilder()
                    .buildGeneralMember();
            memberRepository.save(newMember);
            heartService.addHeartToRecipe(newMember.getId(), recipe.getId());
        }

        //expected
        ResultActions result = mockMvc.perform(delete("/api/recipes/{recipeId}/heart", recipe.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipeId").value(recipe.getId()))
                .andExpect(jsonPath("$.heartCounts").value(prevHeartCounts - 1));

        // docs
        result.andDo(document("heart-remove",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                pathParameters(
                        parameterWithName("recipeId").description("하트를 추가할 레시피 ID")
                ),
                responseFields(
                        fieldWithPath("recipeId").type(JsonFieldType.NUMBER).description("하트가 제거된 레시피 ID"),
                        fieldWithPath("heartCounts").type(JsonFieldType.NUMBER).description("레시피에 달린 총 하트 개수")
                )
        ));
    }
}