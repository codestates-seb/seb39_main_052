package com.seb39.myfridge.recipe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seb39.myfridge.heart.service.HeartService;
import com.seb39.myfridge.ingredient.entity.Ingredient;
import com.seb39.myfridge.ingredient.entity.RecipeIngredient;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.repository.MemberRepository;
import com.seb39.myfridge.recipe.dto.RecipeDto;
import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.recipe.mapper.RecipeMapper;
import com.seb39.myfridge.recipe.repository.RecipeRepository;
import com.seb39.myfridge.recipe.service.RecipeService;
import com.seb39.myfridge.step.entity.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.seb39.myfridge.util.ApiDocumentUtils.getRequestPreProcessor;
import static com.seb39.myfridge.util.ApiDocumentUtils.getResponsePreProcessor;

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
@AutoConfigureRestDocs(uriHost = "api.myfridge.com", uriScheme = "https", uriPort = 443)
@WithUserDetails(value = "test@email.com", userDetailsServiceBeanName = "principalDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
class RecipeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    @MockBean
    private HeartService heartService;

    @SpyBean
    private RecipeMapper recipeMapper;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RecipeRepository recipeRepository;

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
    public void 레시피등록_테스트() throws Exception {
        //given
        List<RecipeDto.Step> stepList = new ArrayList<>();

        MockMultipartFile image = new MockMultipartFile("files", "추가하고 싶은 이미지", "image/png", new FileInputStream("src/test/resources/image/puppy.jpeg"));

        RecipeDto.Step step1 = RecipeDto.Step.builder()
                .sequence(1)
                .content("물을 끓인다")
                .imagePath("https://seb52bucket.s3.ap-northeast-2.amazonaws.com/images/ffc76307-6043-437d-944f-ebc2bd2e0359.jpeg")
                .build();
        stepList.add(step1);

        RecipeDto.Step step2 = RecipeDto.Step.builder()
                .sequence(2)
                .content("끓는 물에 스프를 넣는다.")
                .imagePath("https://seb52bucket.s3.ap-northeast-2.amazonaws.com/images/ffc76307-6043-437d-944f-ebc2bd2e0359.jpeg")
                .build();
        stepList.add(step2);

        RecipeDto.Step step3 = RecipeDto.Step.builder()
                .sequence(3)
                .content("면을 넣는다.")
                .imagePath("https://seb52bucket.s3.ap-northeast-2.amazonaws.com/images/ffc76307-6043-437d-944f-ebc2bd2e0359.jpeg")
                .build();
        stepList.add(step3);

        RecipeDto.Ingredient ingredient1 = RecipeDto.Ingredient.builder()
                .name("고추가루")
                .quantity("2꼬집")
                .build();

        RecipeDto.Ingredient ingredient2 = RecipeDto.Ingredient.builder()
                .name("대파")
                .quantity("10g")
                .build();

        List<RecipeDto.Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);


        RecipeDto.Post requestBody = new RecipeDto.Post("라면 맛있게 끓이는 법", "https://seb52bucket.s3.ap-northeast-2.amazonaws.com/images/ffc76307-6043-437d-944f-ebc2bd2e0359.jpeg", 1, "5분", stepList, ingredients);


        Member member = memberRepository.findByEmail("test@email.com").get();

        RecipeDto.ResponseDetail response = RecipeDto.ResponseDetail.builder()
                .id(1L)
                .title("라면 맛있게 끓이는 법")
                .portion(1)
                .time("5분")
                .createdAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .imagePath("https://seb52bucket.s3.ap-northeast-2.amazonaws.com/images/ffc76307-6043-437d-944f-ebc2bd2e0359.jpeg")
                .ingredients(ingredients)
                .steps(stepList)
                .member(member)
                .build();


        System.out.println("response.getSteps().size() = " + response.getSteps().size());

        willReturn(new Recipe())
                .given(recipeMapper).recipePostToRecipe(any());
        given(recipeService.createRecipe(any(), anyList(), anyLong(), anyList(), anyList())).willReturn(new Recipe());
        willReturn(response)
                .given(recipeMapper).recipeToRecipeResponse(any());

        String requestToJson = objectMapper.writeValueAsString(requestBody);

        MockMultipartFile json = new MockMultipartFile("requestBody", "jsonData", "application/json", requestToJson.getBytes(StandardCharsets.UTF_8));

        //when
        ResultActions actions = mockMvc.perform(multipart("/api/recipes")
                .file(image)
                .file(json)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"));
        //then
        actions.andExpect(status().isOk())
                .andDo(document("recipe-create",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        responseFields(
                                List.of(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("레시피 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("레시피 제목"),
                                        fieldWithPath("portion").type(JsonFieldType.NUMBER).description("해당 레시피가 몇 인분인지"),
                                        fieldWithPath("view").type(JsonFieldType.NUMBER).description("조회수"),
                                        fieldWithPath("time").type(JsonFieldType.STRING).description("요리 소요 시간"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("레시피 생성일"),
                                        fieldWithPath("lastModifiedAt").type(JsonFieldType.STRING).description("레시피 수정일"),
                                        fieldWithPath("imagePath").type(JsonFieldType.STRING).description("레시피 대표 이미지"),
                                        fieldWithPath("ingredients").type(JsonFieldType.ARRAY).description("요리 재료"),
                                        fieldWithPath("ingredients.[].name").type(JsonFieldType.STRING).description("요리 재료 이름"),
                                        fieldWithPath("ingredients.[].quantity").type(JsonFieldType.STRING).description("요리 재료 수량"),
                                        fieldWithPath("steps").type(JsonFieldType.ARRAY).description("요리 단계"),
                                        fieldWithPath("steps.[].sequence").type(JsonFieldType.NUMBER).description("요리 단계 순서"),
                                        fieldWithPath("steps.[].content").type(JsonFieldType.STRING).description("각 단계별 내용"),
                                        fieldWithPath("steps.[].imagePath").type(JsonFieldType.STRING).description("요리 관련 이미지"),
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("작성자 식별자"),
                                        fieldWithPath("memberName").type(JsonFieldType.STRING).description("작성자 이름"),
                                        fieldWithPath("heartCounts").type(JsonFieldType.NUMBER).description("받은 하트 개수")
                                )
                        )
                ));
    }

    @Test
    public void 레시피수정_테스트() throws Exception {
        //given
        List<RecipeDto.Step> stepList = new ArrayList<>();

        MockMultipartFile image = new MockMultipartFile("files", "추가하고 싶은 이미지", "image/png", new FileInputStream("src/test/resources/image/puppy.jpeg"));


        RecipeDto.Step step1 = RecipeDto.Step.builder()
                .sequence(1)
                .content("물을 끓인다")
                .imagePath("https://seb52bucket.s3.ap-northeast-2.amazonaws.com/images/ffc76307-6043-437d-944f-ebc2bd2e0359.jpeg")
                .build();
        stepList.add(step1);

        RecipeDto.Step step2 = RecipeDto.Step.builder()
                .sequence(2)
                .content("끓는 물에 스프를 넣는다.")
                .imagePath("https://seb52bucket.s3.ap-northeast-2.amazonaws.com/images/ffc76307-6043-437d-944f-ebc2bd2e0359.jpeg")
                .build();
        stepList.add(step2);

        RecipeDto.Step step3 = RecipeDto.Step.builder()
                .sequence(3)
                .content("면을 넣는다.")
                .imagePath("https://seb52bucket.s3.ap-northeast-2.amazonaws.com/images/ffc76307-6043-437d-944f-ebc2bd2e0359.jpeg")
                .build();
        stepList.add(step3);

        RecipeDto.Ingredient ingredient1 = RecipeDto.Ingredient.builder()
                .name("고추가루")
                .quantity("2꼬집")
                .build();

        RecipeDto.Ingredient ingredient2 = RecipeDto.Ingredient.builder()
                .name("대파")
                .quantity("10g")
                .build();

        List<RecipeDto.Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);

        RecipeDto.Post requestBody = new RecipeDto.Post("라면 맛있게 끓이는 법", "https://seb52bucket.s3.ap-northeast-2.amazonaws.com/images/ffc76307-6043-437d-944f-ebc2bd2e0359.jpeg", 1, "5분", stepList, ingredients);
        String requestToJson = objectMapper.writeValueAsString(requestBody);
        MockMultipartFile json = new MockMultipartFile("requestBody", "jsonData", "application/json", requestToJson.getBytes(StandardCharsets.UTF_8));

        RecipeDto.Patch patch = RecipeDto.Patch.builder()
                .id(1L)
                .title("라면 백종원처럼 끓이는 법!")
                .imagePath("https://seb52bucket.s3.ap-northeast-2.amazonaws.com/images/ffc76307-6043-437d-944f-ebc2bd2e0359.jpeg")
                .build();

        Member member = memberRepository.findByEmail("test@email.com").get();

        RecipeDto.ResponseDetail response = RecipeDto.ResponseDetail.builder()
                .id(1L)
                .title("라면 맛있게 끓이는 법")
                .portion(1)
                .time("5분")
                .createdAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .imagePath("https://seb52bucket.s3.ap-northeast-2.amazonaws.com/images/ffc76307-6043-437d-944f-ebc2bd2e0359.jpeg")
                .ingredients(ingredients)
                .steps(stepList)
                .member(member)
                .build();

        given(recipeMapper.recipePatchToRecipe(Mockito.any(RecipeDto.Patch.class))).willReturn(new Recipe());
        given(recipeService.updateRecipe(any(), anyList(), anyLong(), anyList(), anyList())).willReturn(new Recipe());

        willReturn(response)
                .given(recipeMapper).recipeToRecipeResponse(any());

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/recipes/{id}", 1L);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PATCH");
                return request;
            }
        });
        //when
        ResultActions actions = mockMvc.perform(builder
                .file(image)
                .file(json)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"));

        //then
        actions.andExpect(status().isOk())
                .andDo(document("recipe-update",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        responseFields(
                                List.of(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("레시피 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("레시피 제목"),
                                        fieldWithPath("portion").type(JsonFieldType.NUMBER).description("해당 레시피가 몇 인분인지"),
                                        fieldWithPath("view").type(JsonFieldType.NUMBER).description("조회수"),
                                        fieldWithPath("time").type(JsonFieldType.STRING).description("요리 소요 시간"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("레시피 생성일"),
                                        fieldWithPath("lastModifiedAt").type(JsonFieldType.STRING).description("레시피 수정일"),
                                        fieldWithPath("imagePath").type(JsonFieldType.STRING).description("레시피 대표 이미지"),
                                        fieldWithPath("ingredients").type(JsonFieldType.ARRAY).description("요리 재료"),
                                        fieldWithPath("ingredients.[].name").type(JsonFieldType.STRING).description("요리 재료 이름"),
                                        fieldWithPath("ingredients.[].quantity").type(JsonFieldType.STRING).description("요리 재료 수량"),
                                        fieldWithPath("steps").type(JsonFieldType.ARRAY).description("요리 단계"),
                                        fieldWithPath("steps.[].sequence").type(JsonFieldType.NUMBER).description("요리 단계 순서"),
                                        fieldWithPath("steps.[].content").type(JsonFieldType.STRING).description("각 단계별 내용"),
                                        fieldWithPath("steps.[].imagePath").type(JsonFieldType.STRING).description("요리 관련 이미지"),
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("작성자 식별자"),
                                        fieldWithPath("memberName").type(JsonFieldType.STRING).description("작성자 이름"),
                                        fieldWithPath("heartCounts").type(JsonFieldType.NUMBER).description("받은 하트 개수")
                                )
                        )
                ));
    }

    @Test
    public void 레시피삭제_테스트() throws Exception {
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

    @Test
    @DisplayName("레시피 상세 조회 테스트")
    void getRecipeDetail() throws Exception {
        // given
        Member member = memberRepository.findAll().get(0);

        Long recipeId = 23L;
        int view = 7645;
        Recipe recipe = new Recipe();
        recipe.setTitle("Recipe 01");
        recipe.setView(view);
        recipe.setMember(member);
        recipe.setTime("30분");
        recipe.setPortion(3);
        recipe.setImagePath("https://seb52bucket.s3./.../main.jpeg");
        ReflectionTestUtils.setField(recipe, "id", recipeId);
        ReflectionTestUtils.setField(recipe, "createdAt", LocalDateTime.now());
        ReflectionTestUtils.setField(recipe, "lastModifiedAt", LocalDateTime.now());

        for (int i = 1; i <= 3; i++) {
            Step step = new Step();
            step.setContent("Step " + i);
            step.setSequence(i);
            step.addRecipe(recipe);
            step.setImagePath("https://seb52bucket.s3./.../step.jpeg");
        }

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("Ingredient 1");
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("Ingredient 2");
        List<Ingredient> ingredients = List.of(ingredient1, ingredient2);

        for (int i = 0; i < 2; i++) {
            RecipeIngredient ri = new RecipeIngredient();
            ri.setQuantity((i + 1) + "00g");
            ri.addRecipe(recipe);
            ri.addIngredient(ingredients.get(i));
        }

        given(recipeService.findRecipeWithDetails(anyLong()))
                .willReturn(recipe);
        int heartCounts = 321;
        given(heartService.findHeartCounts(anyLong()))
                .willReturn(heartCounts);

        //expected
        ResultActions actions = mockMvc.perform(get("/api/recipes/{id}", recipeId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").value(recipeId))
                .andExpect(jsonPath("heartCounts").value(heartCounts))
                .andExpect(jsonPath("view").value(view));


        //docs
        actions.andExpect(status().isOk())
                .andDo(document("recipe-read-detail",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("id").description("조회할 레시피의 ID")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("레시피 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("레시피 제목"),
                                        fieldWithPath("portion").type(JsonFieldType.NUMBER).description("해당 레시피가 몇 인분인지"),
                                        fieldWithPath("view").type(JsonFieldType.NUMBER).description("조회수"),
                                        fieldWithPath("time").type(JsonFieldType.STRING).description("요리 소요 시간"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("레시피 생성일"),
                                        fieldWithPath("lastModifiedAt").type(JsonFieldType.STRING).description("레시피 수정일"),
                                        fieldWithPath("imagePath").type(JsonFieldType.STRING).description("레시피 대표 이미지"),
                                        fieldWithPath("ingredients").type(JsonFieldType.ARRAY).description("요리 재료"),
                                        fieldWithPath("ingredients.[].name").type(JsonFieldType.STRING).description("요리 재료 이름"),
                                        fieldWithPath("ingredients.[].quantity").type(JsonFieldType.STRING).description("요리 재료 수량"),
                                        fieldWithPath("steps").type(JsonFieldType.ARRAY).description("요리 단계"),
                                        fieldWithPath("steps.[].sequence").type(JsonFieldType.NUMBER).description("요리 단계 순서"),
                                        fieldWithPath("steps.[].content").type(JsonFieldType.STRING).description("각 단계별 내용"),
                                        fieldWithPath("steps.[].imagePath").type(JsonFieldType.STRING).description("요리 관련 이미지"),
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("작성자 식별자"),
                                        fieldWithPath("memberName").type(JsonFieldType.STRING).description("작성자 이름"),
                                        fieldWithPath("heartCounts").type(JsonFieldType.NUMBER).description("받은 하트 개수")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("특정 단어가 포함된 레시피 제목 검색")
    void findTitlesTest() throws Exception {
        // given
        willReturn(List.of(
                "Creamy Chicken Penne Pasta",
                "Cheesy Chicken Alfredo Pasta Bake",
                "One Pot Garlic Parmesan Pasta",
                "Penne With Tomato Sauce Pasta"))
                .given(recipeService).findTitlesByContainsWord("pasta");

        // expected
        ResultActions actions = mockMvc.perform(get("/api/recipes/titles")
                        .param("word", "pasta")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(4));

        //docs
        actions.andExpect(status().isOk())
                .andDo(document("recipe-search-titles",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestParameters(
                                parameterWithName("word").description("찾을 레시피의 제목")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("레시피 제목 리스트")
                                )
                        )
                ));
    }
}