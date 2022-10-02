package com.seb39.myfridge.heart.controller;

import com.seb39.myfridge.heart.entity.Heart;
import com.seb39.myfridge.heart.repository.HeartRepository;
import com.seb39.myfridge.heart.service.HeartService;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.repository.MemberRepository;
import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.recipe.repository.RecipeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
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
import static org.assertj.core.api.Assertions.*;
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
class HeartControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    HeartRepository heartRepository;

    @Autowired
    HeartService heartService;

    @BeforeEach
    void beforeEach(){
        Member member = Member.generalBuilder()
                .email("test@gmail.com")
                .buildGeneralMember();
        memberRepository.save(member);

        Recipe recipe = new Recipe();
        recipe.setMember(member);
        recipeRepository.save(recipe);

        System.out.println("BeforeEach MemberId = " + member.getId() + " RecipeId = " + recipe.getId());
    }

    @AfterEach
    void afterEach(){
        memberRepository.deleteAll();
        recipeRepository.deleteAll();
    }

    @Test
    @DisplayName("레시피에 하트를 추가한다")
    @WithUserDetails(value = "test@gmail.com", userDetailsServiceBeanName = "principalDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void postHeart() throws Exception {
        //given
        Member member = memberRepository.findAll().get(0);
        Recipe recipe = recipeRepository.findAll().get(0);

        System.out.println("PostHeart MemberId = " + member.getId() + " RecipeId = " + recipe.getId());

        //expected
        ResultActions result = mockMvc.perform(post("/api/recipes/{recipeId}/heart", recipe.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipeId").value(recipe.getId()))
                .andExpect(jsonPath("$.heartCounts").value(1));

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
    @WithUserDetails(value = "test@gmail.com", userDetailsServiceBeanName = "principalDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deleteHeart() throws Exception {
        //given
        Member member = memberRepository.findAll().get(0);
        Recipe recipe = recipeRepository.findAll().get(0);

        System.out.println("DeleteHeart MemberId = " + member.getId() + " RecipeId = " + recipe.getId());

        heartRepository.save(new Heart(member,recipe));

        List<Heart> hearts = heartRepository.findAll();
        System.out.println("------- hearts -------");
        for (Heart heart : hearts) {
            System.out.println("  member: " + heart.getMember().getId() + " / recipe: " + heart.getRecipe().getId());
        }

        //expected
        ResultActions result = mockMvc.perform(delete("/api/recipes/{recipeId}/heart", recipe.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipeId").value(recipe.getId()))
                .andExpect(jsonPath("$.heartCounts").value(0));

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