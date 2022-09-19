package com.seb39.myfridge.recipe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.repository.MemberRepository;
import com.seb39.myfridge.recipe.dto.RecipeDto;
import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.recipe.mapper.RecipeMapper;
import com.seb39.myfridge.recipe.service.RecipeService;
import com.seb39.myfridge.step.entity.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.seb39.myfridge.util.ApiDocumentUtils.getRequestPreProcessor;
import static com.seb39.myfridge.util.ApiDocumentUtils.getResponsePreProcessor;
import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs(uriHost = "api.myfridge.com")
@WithUserDetails(value = "test@email.com", userDetailsServiceBeanName = "principalDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
class RecipeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    @MockBean
    private RecipeMapper recipeMapper;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void 회원데이터_준비() {
        Member member = Member.generalBuilder()
                .email("test@email.com")
                .name("testA")
                .password("1234")
                .buildGeneralMember();
        memberRepository.save(member);
    }

    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
    }

    @Test
    public void 레시피등록_테스트() throws Exception{
        //given
        List<RecipeDto.Step> stepList = new ArrayList<>();

        RecipeDto.Step step1 = RecipeDto.Step.builder()
                .sequence(1)
                .content("물을 끓인다")
                .imagePath("imagePath1")
                .build();
        stepList.add(step1);

        RecipeDto.Step step2 = RecipeDto.Step.builder()
                .sequence(2)
                .content("끓는 물에 스프를 넣는다.")
                .imagePath("imagePath2")
                .build();
        stepList.add(step2);

        RecipeDto.Step step3 = RecipeDto.Step.builder()
                .sequence(3)
                .content("면을 넣는다.")
                .imagePath("imagePath3")
                .build();
        stepList.add(step3);

        RecipeDto.Post requestBody = new RecipeDto.Post("라면 맛있게 끓이는 법","imagePath",1,"5분", stepList);


        Member member = memberRepository.findByEmail("test@email.com").get();

        RecipeDto.Response response = new RecipeDto.Response(
                1L,
                "라면 맛있게 끓이는 법",
                1,
                "5분",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "recipe imagePath",
                stepList,
                member
        );
        System.out.println("response.getSteps().size() = " + response.getSteps().size());

        given(recipeMapper.recipePostToRecipe(any())).willReturn(new Recipe());
        given(recipeService.createRecipe(any(), anyList(), anyLong())).willReturn(new Recipe());
        given(recipeMapper.recipeToRecipeResponse(Mockito.any(Recipe.class))).willReturn(response);
        String requestToJson = objectMapper.writeValueAsString(requestBody);

        //when
        ResultActions actions = mockMvc.perform(post("/api/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestToJson));
        //then
        actions.andExpect(status().isOk())
                .andDo(document("recipe-create",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestFields(
                                List.of(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("레시피 제목"),
                                        fieldWithPath("imagePath").type(JsonFieldType.STRING).description("레시피 대표 이미지"),
                                        fieldWithPath("portion").type(JsonFieldType.NUMBER).description("해당 레시피가 몇 인분인지"),
                                        fieldWithPath("time").type(JsonFieldType.STRING).description("요리 소요 시간"),
                                        fieldWithPath("steps").type(JsonFieldType.ARRAY).description("요리 단계"),
                                        fieldWithPath("steps.[].sequence").type(JsonFieldType.NUMBER).description("요리 단계 순서"),
                                        fieldWithPath("steps.[].content").type(JsonFieldType.STRING).description("각 단계별 내용"),
                                        fieldWithPath("steps.[].imagePath").type(JsonFieldType.STRING).description("요리 관련 이미지")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("레시피 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("레시피 제목"),
                                        fieldWithPath("portion").type(JsonFieldType.NUMBER).description("해당 레시피가 몇 인분인지"),
                                        fieldWithPath("time").type(JsonFieldType.STRING).description("요리 소요 시간"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("레시피 생성일"),
                                        fieldWithPath("lastModifiedAt").type(JsonFieldType.STRING).description("레시피 수정일"),
                                        fieldWithPath("imagePath").type(JsonFieldType.STRING).description("레시피 대표 이미지"),
                                        fieldWithPath("steps").type(JsonFieldType.ARRAY).description("요리 단계"),
                                        fieldWithPath("steps.[].sequence").type(JsonFieldType.NUMBER).description("요리 단계 순서"),
                                        fieldWithPath("steps.[].content").type(JsonFieldType.STRING).description("각 단계별 내용"),
                                        fieldWithPath("steps.[].imagePath").type(JsonFieldType.STRING).description("요리 관련 이미지"),
                                        fieldWithPath("memberName").type(JsonFieldType.STRING).description("작성자 이름")
                                )
                        )
                        ));
    }
    @Test
    public void 레시피수정_테스트() throws Exception{
        //given
        List<RecipeDto.Step> stepList = new ArrayList<>();

        RecipeDto.Step step1 = RecipeDto.Step.builder()
                .sequence(1)
                .content("물을 끓인다")
                .imagePath("imagePath1")
                .build();
        stepList.add(step1);

        RecipeDto.Step step2 = RecipeDto.Step.builder()
                .sequence(2)
                .content("끓는 물에 스프를 넣는다.")
                .imagePath("imagePath2")
                .build();
        stepList.add(step2);

        RecipeDto.Step step3 = RecipeDto.Step.builder()
                .sequence(3)
                .content("면을 넣는다.")
                .imagePath("imagePath3")
                .build();
        stepList.add(step3);

        RecipeDto.Post requestBody = new RecipeDto.Post("라면 맛있게 끓이는 법","imagePath",1,"5분", stepList);
        String requestToJson = objectMapper.writeValueAsString(requestBody);

        RecipeDto.Patch patch = RecipeDto.Patch.builder()
                .id(1L)
                .title("라면 백종원처럼 끓이는 법!")
                .imagePath("recipe image")
                .build();

        Member member = memberRepository.findByEmail("test@email.com").get();

        RecipeDto.Response response = new RecipeDto.Response(
                1L,
                patch.getTitle(),
                1,
                "5분",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "recipe imagePath",
                stepList,
                member
        );
        given(recipeMapper.recipePatchToRecipe(Mockito.any(RecipeDto.Patch.class))).willReturn(new Recipe());
        given(recipeService.updateRecipe(any(), anyList(), anyLong())).willReturn(new Recipe());
        given(recipeMapper.recipeToRecipeResponse(Mockito.any(Recipe.class))).willReturn(response);
        //when
        ResultActions actions = mockMvc.perform(patch("/api/recipes/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestToJson));

        //then
        actions.andExpect(status().isOk())
                .andDo(document("recipe-update",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("id").description("수정할 레시피 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("수정할 레시피 제목"),
                                        fieldWithPath("imagePath").type(JsonFieldType.STRING).description("수정할 레시피 대표 이미지"),
                                        fieldWithPath("portion").type(JsonFieldType.NUMBER).description("해당 레시피가 몇 인분인지"),
                                        fieldWithPath("time").type(JsonFieldType.STRING).description("수정할 요리 소요 시간"),
                                        fieldWithPath("steps").type(JsonFieldType.ARRAY).description("수정할 요리 단계"),
                                        fieldWithPath("steps.[].sequence").type(JsonFieldType.NUMBER).description("요리 단계 순서"),
                                        fieldWithPath("steps.[].content").type(JsonFieldType.STRING).description("각 단계별 내용"),
                                        fieldWithPath("steps.[].imagePath").type(JsonFieldType.STRING).description("요리 관련 이미지")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("레시피 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("레시피 제목"),
                                        fieldWithPath("portion").type(JsonFieldType.NUMBER).description("해당 레시피가 몇 인분인지"),
                                        fieldWithPath("time").type(JsonFieldType.STRING).description("요리 소요 시간"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("레시피 생성일"),
                                        fieldWithPath("lastModifiedAt").type(JsonFieldType.STRING).description("레시피 수정일"),
                                        fieldWithPath("imagePath").type(JsonFieldType.STRING).description("레시피 대표 이미지"),
                                        fieldWithPath("steps").type(JsonFieldType.ARRAY).description("요리 단계"),
                                        fieldWithPath("steps.[].sequence").type(JsonFieldType.NUMBER).description("요리 단계 순서"),
                                        fieldWithPath("steps.[].content").type(JsonFieldType.STRING).description("각 단계별 내용"),
                                        fieldWithPath("steps.[].imagePath").type(JsonFieldType.STRING).description("요리 관련 이미지"),
                                        fieldWithPath("memberName").type(JsonFieldType.STRING).description("작성자 이름")
                                )
                        )
                ));
    }

    @Test
    public void 질문삭제_테스트() throws Exception {
        //given
        long recipeId = 1L;
        Long memberId = memberRepository.findByEmail("test@email.com").get().getId();
        //when
        doNothing().when(recipeService).deleteRecipe(recipeId, memberId);
        ResultActions actions = mockMvc.perform(delete("/api/recipes/{id}", recipeId));
        //then
        actions.andExpect(status().isOk())
                .andDo(document("recipe-delete",
                        pathParameters(
                                parameterWithName("id").description("삭제할 레시피 식별자")
                        )));
    }
}