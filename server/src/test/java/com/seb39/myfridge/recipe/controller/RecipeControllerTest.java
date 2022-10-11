package com.seb39.myfridge.recipe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.seb39.myfridge.fridge.entity.Fridge;
import com.seb39.myfridge.fridge.service.FridgeService;
import com.seb39.myfridge.heart.entity.Heart;
import com.seb39.myfridge.image.entity.Image;
import com.seb39.myfridge.heart.service.HeartService;
import com.seb39.myfridge.ingredient.entity.Ingredient;
import com.seb39.myfridge.ingredient.entity.RecipeIngredient;
import com.seb39.myfridge.member.dto.MemberDto;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.repository.MemberRepository;
import com.seb39.myfridge.recipe.dto.MyRecipeDto;
import com.seb39.myfridge.recipe.dto.RecipeDto;
import com.seb39.myfridge.recipe.dto.RecipeRecommendDto;
import com.seb39.myfridge.recipe.dto.RecipeSearch;
import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.recipe.enums.RecipeSort;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.HttpStatus;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @MockBean
    private FridgeService fridgeService;

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

        Image inputImage0 = new Image();
        inputImage0.setImagePath("src/test/resources/image/puppy.jpeg");
        inputImage0.setIdx(0);
        inputImage0.setIsUpdated("N");
        RecipeDto.ImageInfo imageInfo0 = RecipeDto.ImageInfo.builder()
                .imagePath(inputImage0.getImagePath())
                .idx(inputImage0.getIdx())
                .isUpdated(inputImage0.getIsUpdated())
                .build();

        Image inputImage1 = new Image();
        inputImage1.setImagePath("src/test/resources/image/puppy.jpeg");
        inputImage1.setIdx(1);
        inputImage1.setIsUpdated("N");

        RecipeDto.ImageInfo imageInfo1 = RecipeDto.ImageInfo.builder()
                .imagePath(inputImage0.getImagePath())
                .idx(inputImage0.getIdx())
                .isUpdated(inputImage0.getIsUpdated())
                .build();

        Image inputImage2 = new Image();
        inputImage2.setImagePath("src/test/resources/image/puppy.jpeg");
        inputImage2.setIdx(2);
        inputImage2.setIsUpdated("N");
        RecipeDto.ImageInfo imageInfo2 = RecipeDto.ImageInfo.builder()
                .imagePath(inputImage0.getImagePath())
                .idx(inputImage0.getIdx())
                .isUpdated(inputImage0.getIsUpdated())
                .build();

        Image inputImage3 = new Image();
        inputImage3.setImagePath("src/test/resources/image/puppy.jpeg");
        inputImage3.setIdx(3);
        inputImage3.setIsUpdated("N");
        RecipeDto.ImageInfo imageInfo3 = RecipeDto.ImageInfo.builder()
                .imagePath(inputImage0.getImagePath())
                .idx(inputImage0.getIdx())
                .isUpdated(inputImage0.getIsUpdated())
                .build();

        MockMultipartFile image = new MockMultipartFile("files", "추가하고 싶은 이미지", "image/png", new FileInputStream("src/test/resources/image/puppy.jpeg"));

        RecipeDto.Step step1 = RecipeDto.Step.builder()
                .sequence(1)
                .content("물을 끓인다")
                .imageInfo(imageInfo1)
                .build();
        stepList.add(step1);

        RecipeDto.Step step2 = RecipeDto.Step.builder()
                .sequence(2)
                .content("끓는 물에 스프를 넣는다.")
                .imageInfo(imageInfo2)
                .build();
        stepList.add(step2);

        RecipeDto.Step step3 = RecipeDto.Step.builder()
                .sequence(3)
                .content("면을 넣는다.")
                .imageInfo(imageInfo3)
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


        RecipeDto.Post requestBody = new RecipeDto.Post("라면 맛있게 끓이는 법", 1, "5분", stepList, ingredients);


        Member member = memberRepository.findGeneralByEmail("test@email.com").get();

        RecipeDto.ResponseDetail response = RecipeDto.ResponseDetail.builder()
                .id(1L)
                .title("라면 맛있게 끓이는 법")
                .portion(1)
                .view(0)
                .time("5분")
                .createdAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .imageInfo(imageInfo0)
                .ingredients(ingredients)
                .steps(stepList)
                .member(new MemberDto.Response(member))
                .heartCounts(0)
                .build();

        willReturn(new Recipe())
                .given(recipeMapper).recipePostToRecipe(any());
        given(recipeService.createRecipe(any(), anyList(), anyLong(), anyList(), anyList())).willReturn(new Recipe());
        willReturn(response)
                .given(recipeMapper).recipeToRecipeResponseDetail(any());

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
                                        fieldWithPath("imageInfo").type(JsonFieldType.OBJECT).description("레시피 대표 이미지 정보"),
                                        fieldWithPath("imageInfo.idx").type(JsonFieldType.NUMBER).description("이미지 인덱스"),
                                        fieldWithPath("imageInfo.imagePath").type(JsonFieldType.STRING).description("이미지 Path"),
                                        fieldWithPath("imageInfo.isUpdated").type(JsonFieldType.STRING).description("이미지 수정 여부"),
                                        fieldWithPath("ingredients").type(JsonFieldType.ARRAY).description("요리 재료"),
                                        fieldWithPath("ingredients.[].name").type(JsonFieldType.STRING).description("요리 재료 이름"),
                                        fieldWithPath("ingredients.[].quantity").type(JsonFieldType.STRING).description("요리 재료 수량"),
                                        fieldWithPath("steps").type(JsonFieldType.ARRAY).description("요리 단계"),
                                        fieldWithPath("steps.[].sequence").type(JsonFieldType.NUMBER).description("요리 단계 순서"),
                                        fieldWithPath("steps.[].content").type(JsonFieldType.STRING).description("각 단계별 내용"),
                                        fieldWithPath("steps.[].imageInfo").type(JsonFieldType.OBJECT).description("요리 단계별 이미지 정보"),
                                        fieldWithPath("steps.[].imageInfo.idx").type(JsonFieldType.NUMBER).description("이미지 인덱스"),
                                        fieldWithPath("steps.[].imageInfo.imagePath").type(JsonFieldType.STRING).description("이미지 Path"),
                                        fieldWithPath("steps.[].imageInfo.isUpdated").type(JsonFieldType.STRING).description("이미지 수정 여부"),
                                        fieldWithPath("member.id").type(JsonFieldType.NUMBER).description("작성자 식별자"),
                                        fieldWithPath("member.name").type(JsonFieldType.STRING).description("작성자 이름"),
                                        fieldWithPath("member.profileImagePath").type(JsonFieldType.STRING).description("작성자 프로필 이미지 경로").optional(),
                                        fieldWithPath("heartCounts").type(JsonFieldType.NUMBER).description("받은 하트 개수"),
                                        fieldWithPath("heartExist").type(JsonFieldType.BOOLEAN).description("(로그인 사용자의 경우) 자신이 하트를 눌렀는지 여부")
                                )
                        )
                ));
    }

    @Test
    public void 레시피수정_테스트() throws Exception {
        //given
        List<RecipeDto.Step> stepList = new ArrayList<>();

        MockMultipartFile image = new MockMultipartFile("files", "추가하고 싶은 이미지", "image/png", new FileInputStream("src/test/resources/image/puppy.jpeg"));

        Image inputImage0 = new Image();
        inputImage0.setImagePath("src/test/resources/image/puppy.jpeg");
        inputImage0.setIdx(0);
        inputImage0.setIsUpdated("N");
        RecipeDto.ImageInfo imageInfo0 = RecipeDto.ImageInfo.builder()
                .imagePath(inputImage0.getImagePath())
                .idx(inputImage0.getIdx())
                .isUpdated(inputImage0.getIsUpdated())
                .build();

        Image inputImage1 = new Image();
        inputImage1.setImagePath("src/test/resources/image/puppy.jpeg");
        inputImage1.setIdx(1);
        inputImage1.setIsUpdated("N");

        RecipeDto.ImageInfo imageInfo1 = RecipeDto.ImageInfo.builder()
                .imagePath(inputImage0.getImagePath())
                .idx(inputImage0.getIdx())
                .isUpdated(inputImage0.getIsUpdated())
                .build();

        Image inputImage2 = new Image();
        inputImage2.setImagePath("src/test/resources/image/puppy.jpeg");
        inputImage2.setIdx(2);
        inputImage2.setIsUpdated("N");
        RecipeDto.ImageInfo imageInfo2 = RecipeDto.ImageInfo.builder()
                .imagePath(inputImage0.getImagePath())
                .idx(inputImage0.getIdx())
                .isUpdated(inputImage0.getIsUpdated())
                .build();

        Image inputImage3 = new Image();
        inputImage3.setImagePath("src/test/resources/image/puppy.jpeg");
        inputImage3.setIdx(3);
        inputImage3.setIsUpdated("N");
        RecipeDto.ImageInfo imageInfo3 = RecipeDto.ImageInfo.builder()
                .imagePath(inputImage0.getImagePath())
                .idx(inputImage0.getIdx())
                .isUpdated(inputImage0.getIsUpdated())
                .build();


        RecipeDto.Step step1 = RecipeDto.Step.builder()
                .sequence(1)
                .content("물을 끓인다")
                .imageInfo(imageInfo1)
                .build();
        stepList.add(step1);

        RecipeDto.Step step2 = RecipeDto.Step.builder()
                .sequence(2)
                .content("끓는 물에 스프를 넣는다.")
                .imageInfo(imageInfo2)
                .build();
        stepList.add(step2);

        RecipeDto.Step step3 = RecipeDto.Step.builder()
                .sequence(3)
                .content("면을 넣는다.")
                .imageInfo(imageInfo3)
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

        RecipeDto.Post requestBody = new RecipeDto.Post("라면 맛있게 끓이는 법", 1, "5분", stepList, ingredients);

        String requestToJson = objectMapper.writeValueAsString(requestBody);
        MockMultipartFile json = new MockMultipartFile("requestBody", "requestToJson", "application/json", requestToJson.getBytes(StandardCharsets.UTF_8));

        RecipeDto.Patch patch = RecipeDto.Patch.builder()
                .id(1L)
                .title("라면 백종원처럼 끓이는 법!")
                .portion(1)
                .time("3분")
                .steps(stepList)
                .imageInfo(imageInfo0)
                .build();

        Member member = memberRepository.findGeneralByEmail("test@email.com").get();

        RecipeDto.ResponseDetail response = RecipeDto.ResponseDetail.builder()
                .id(1L)
                .title("라면 맛있게 끓이는 법")
                .portion(1)
                .view(0)
                .time("5분")
                .createdAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .imageInfo(imageInfo0)
                .ingredients(ingredients)
                .steps(stepList)
                .member(new MemberDto.Response(member))
                .heartCounts(0)
                .build();

        willReturn(new Recipe()).given(recipeMapper).recipePatchToRecipe(Mockito.any(RecipeDto.Patch.class));
//        given(recipeMapper.recipePatchToRecipe(Mockito.any(RecipeDto.Patch.class))).willReturn(new Recipe());
        given(recipeService.updateRecipe(any(), anyList(), anyLong(), anyList(), anyList())).willReturn(new Recipe());

        willReturn(response)
                .given(recipeMapper).recipeToRecipeResponseDetail(any());

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
                                        fieldWithPath("imageInfo").type(JsonFieldType.OBJECT).description("레시피 대표 이미지 정보"),
                                        fieldWithPath("imageInfo.idx").type(JsonFieldType.NUMBER).description("이미지 인덱스"),
                                        fieldWithPath("imageInfo.imagePath").type(JsonFieldType.STRING).description("이미지 Path"),
                                        fieldWithPath("imageInfo.isUpdated").type(JsonFieldType.STRING).description("이미지 수정 여부"),
                                        fieldWithPath("ingredients").type(JsonFieldType.ARRAY).description("요리 재료"),
                                        fieldWithPath("ingredients.[].name").type(JsonFieldType.STRING).description("요리 재료 이름"),
                                        fieldWithPath("ingredients.[].quantity").type(JsonFieldType.STRING).description("요리 재료 수량"),
                                        fieldWithPath("steps").type(JsonFieldType.ARRAY).description("요리 단계"),
                                        fieldWithPath("steps.[].sequence").type(JsonFieldType.NUMBER).description("요리 단계 순서"),
                                        fieldWithPath("steps.[].content").type(JsonFieldType.STRING).description("각 단계별 내용"),
                                        fieldWithPath("steps.[].imageInfo").type(JsonFieldType.OBJECT).description("요리 단계별 이미지 정보"),
                                        fieldWithPath("steps.[].imageInfo.idx").type(JsonFieldType.NUMBER).description("이미지 인덱스"),
                                        fieldWithPath("steps.[].imageInfo.imagePath").type(JsonFieldType.STRING).description("이미지 Path"),
                                        fieldWithPath("steps.[].imageInfo.isUpdated").type(JsonFieldType.STRING).description("이미지 수정 여부"),
                                        fieldWithPath("member.id").type(JsonFieldType.NUMBER).description("작성자 식별자"),
                                        fieldWithPath("member.name").type(JsonFieldType.STRING).description("작성자 이름"),
                                        fieldWithPath("member.profileImagePath").type(JsonFieldType.STRING).description("작성자 프로필 이미지 경로").optional(),
                                        fieldWithPath("heartCounts").type(JsonFieldType.NUMBER).description("받은 하트 개수"),
                                        fieldWithPath("heartExist").type(JsonFieldType.BOOLEAN).description("(로그인 사용자의 경우) 자신이 하트를 눌렀는지 여부")
                                )
                        )
                ));
    }

    @Test
    public void 레시피삭제_테스트() throws Exception {
        //given
        long recipeId = 1L;
        Long memberId = memberRepository.findGeneralByEmail("test@email.com").get().getId();
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
        Image inputImage0 = new Image();
        inputImage0.setImagePath("src/test/resources/image/puppy.jpeg");
        inputImage0.setIdx(0);
        inputImage0.setIsUpdated("N");
        RecipeDto.ImageInfo imageInfo0 = RecipeDto.ImageInfo.builder()
                .imagePath(inputImage0.getImagePath())
                .idx(inputImage0.getIdx())
                .isUpdated(inputImage0.getIsUpdated())
                .build();

        Image inputImage1 = new Image();
        inputImage1.setImagePath("src/test/resources/image/puppy.jpeg");
        inputImage1.setIdx(1);
        inputImage1.setIsUpdated("N");

        RecipeDto.ImageInfo imageInfo1 = RecipeDto.ImageInfo.builder()
                .imagePath(inputImage0.getImagePath())
                .idx(inputImage0.getIdx())
                .isUpdated(inputImage0.getIsUpdated())
                .build();

        Image inputImage2 = new Image();
        inputImage2.setImagePath("src/test/resources/image/puppy.jpeg");
        inputImage2.setIdx(2);
        inputImage2.setIsUpdated("N");
        RecipeDto.ImageInfo imageInfo2 = RecipeDto.ImageInfo.builder()
                .imagePath(inputImage0.getImagePath())
                .idx(inputImage0.getIdx())
                .isUpdated(inputImage0.getIsUpdated())
                .build();

        Image inputImage3 = new Image();
        inputImage3.setImagePath("src/test/resources/image/puppy.jpeg");
        inputImage3.setIdx(3);
        inputImage3.setIsUpdated("N");
        RecipeDto.ImageInfo imageInfo3 = RecipeDto.ImageInfo.builder()
                .imagePath(inputImage0.getImagePath())
                .idx(inputImage0.getIdx())
                .isUpdated(inputImage0.getIsUpdated())
                .build();


        Long recipeId = 23L;
        int view = 7645;
        Recipe recipe = new Recipe();
        recipe.setTitle("Recipe 01");
        recipe.setView(view);
        recipe.setMember(member);
        recipe.setTime("30분");
        recipe.setPortion(3);
        recipe.setImage(inputImage0);
        ReflectionTestUtils.setField(recipe, "id", recipeId);
        ReflectionTestUtils.setField(recipe, "createdAt", LocalDateTime.now());
        ReflectionTestUtils.setField(recipe, "lastModifiedAt", LocalDateTime.now());

        for (int i = 1; i <= 3; i++) {
            Step step = new Step();
            step.setContent("Step " + i);
            step.setSequence(i);
            step.addRecipe(recipe);
            step.setImage(inputImage0);
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
                                        fieldWithPath("imageInfo").type(JsonFieldType.OBJECT).description("레시피 대표 이미지 정보"),
                                        fieldWithPath("imageInfo.idx").type(JsonFieldType.NUMBER).description("이미지 인덱스"),
                                        fieldWithPath("imageInfo.imagePath").type(JsonFieldType.STRING).description("이미지 Path"),
                                        fieldWithPath("imageInfo.isUpdated").type(JsonFieldType.STRING).description("이미지 수정 여부"),
                                        fieldWithPath("ingredients").type(JsonFieldType.ARRAY).description("요리 재료"),
                                        fieldWithPath("ingredients.[].name").type(JsonFieldType.STRING).description("요리 재료 이름"),
                                        fieldWithPath("ingredients.[].quantity").type(JsonFieldType.STRING).description("요리 재료 수량"),
                                        fieldWithPath("steps").type(JsonFieldType.ARRAY).description("요리 단계"),
                                        fieldWithPath("steps.[].sequence").type(JsonFieldType.NUMBER).description("요리 단계 순서"),
                                        fieldWithPath("steps.[].content").type(JsonFieldType.STRING).description("각 단계별 내용"),
                                        fieldWithPath("steps.[].imageInfo").type(JsonFieldType.OBJECT).description("요리 단계별 이미지 정보"),
                                        fieldWithPath("steps.[].imageInfo.idx").type(JsonFieldType.NUMBER).description("이미지 인덱스"),
                                        fieldWithPath("steps.[].imageInfo.imagePath").type(JsonFieldType.STRING).description("이미지 Path"),
                                        fieldWithPath("steps.[].imageInfo.isUpdated").type(JsonFieldType.STRING).description("이미지 수정 여부"),
                                        fieldWithPath("member.id").type(JsonFieldType.NUMBER).description("작성자 식별자"),
                                        fieldWithPath("member.name").type(JsonFieldType.STRING).description("작성자 이름"),
                                        fieldWithPath("member.profileImagePath").type(JsonFieldType.STRING).description("작성자 프로필 이미지 경로").optional(),
                                        fieldWithPath("heartCounts").type(JsonFieldType.NUMBER).description("받은 하트 개수"),
                                        fieldWithPath("heartExist").type(JsonFieldType.BOOLEAN).description("(로그인 사용자의 경우) 자신이 하트를 눌렀는지 여부")
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
                        .accept(MediaType.APPLICATION_JSON))
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

    @Test
    @DisplayName("제목과 태그로 레시피 리스트 검색")
    void searchRecipesTest() throws Exception {
        //given
        RecipeSearch.Request request = new RecipeSearch.Request();
        request.setTitle("김치");
        request.setPage(1);
        request.setIngredients(List.of("김치", "간장"));
        request.setSort(RecipeSort.VIEW);
        String requestJson = objectMapper.writeValueAsString(request);

        List<RecipeSearch.Response> content = new ArrayList<>();
        for (int i = 1; i <= 16; i++) {
            RecipeSearch.Response dto = new RecipeSearch.Response((long) i, "김치찌개 " + i, (long) i, "member" + i, "https://s3.aws.abcd/member" + i + ".jpeg", "https://s3.aws.abcd/recipe" + i + ".jpeg", 10, 17 - i, LocalDateTime.now());
            content.add(dto);
        }
        Page<RecipeSearch.Response> page = PageableExecutionUtils.getPage(content, PageRequest.of(0, 16), () -> 40);
        willReturn(page).given(recipeService).searchRecipes(any());

        List<Heart> hearts = new ArrayList<>();
        Member member = memberRepository.findAll().get(0);
        for (int i = 1; i <= 16; i += 2) {
            Recipe recipe = new Recipe();
            ReflectionTestUtils.setField(recipe, "id", (long) i);
            hearts.add(new Heart(member, recipe));
        }
        willReturn(hearts).given(heartService).findHearts(any(), any());

        // expected
        ResultActions actions = mockMvc.perform(post("/api/recipes/search")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(jsonPath("$.data").isArray());

        //then
        actions.andDo(document("recipe-search-list",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("검색할 레시피 제목"),
                        fieldWithPath("ingredients").type(JsonFieldType.ARRAY).description("검색할 재료 이름 리스트"),
                        fieldWithPath("page").type(JsonFieldType.NUMBER).description("검색할 페이지 번호"),
                        fieldWithPath("sort").type(JsonFieldType.STRING).description("검색결과 정렬 기준. (VIEW 조회수, RECENT 최신순, HEART 하트 많은 순)")
                ),
                responseFields(
                        fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("레시피 리스트"),
                        fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("검색된 레시피의 ID"),
                        fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("검색된 레시피의 제목"),
                        fieldWithPath("data.[].member.id").type(JsonFieldType.NUMBER).description("레시피 작성자 ID"),
                        fieldWithPath("data.[].member.name").type(JsonFieldType.STRING).description("레시피 작성자 이름"),
                        fieldWithPath("data.[].member.profileImagePath").type(JsonFieldType.STRING).description("레시피 작성자 프로필 이미지 경로").optional(),
                        fieldWithPath("data.[].imagePath").type(JsonFieldType.STRING).description("레시피의 대표 이미지 경로"),
                        fieldWithPath("data.[].heartCounts").type(JsonFieldType.NUMBER).description("레시피의 하트 수"),
                        fieldWithPath("data.[].view").type(JsonFieldType.NUMBER).description("조회수"),
                        fieldWithPath("data.[].lastModifiedAt").type(JsonFieldType.STRING).description("레시피 마지막 수정 일자"),
                        fieldWithPath("data.[].heartExist").type(JsonFieldType.BOOLEAN).description("나의 해당 레시피 하트 여부 (미로그인시 false)"),
                        fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                        fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("현재 사이즈"),
                        fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("레시피 전체 수"),
                        fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 개수")
                )
        ));
    }

    @Test
    @DisplayName("내가 작성한 레시피 리스트 검색")
    void getMyRecipesTest() throws Exception {
        //given
        int page = 1;
        int size = 4;
        String sort = "RECENT";
        Long memberId = memberRepository.findAll().get(0).getId();

        List<MyRecipeDto.Mine> content = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            MyRecipeDto.Mine dto = new MyRecipeDto.Mine((long) i, "볶음밥 " + i, "https://s3.aws.abcdefg/recipe" + i + ".jpeg", 1234, 10 - i, 5 - i, LocalDateTime.now());
            content.add(dto);
        }
        Page<MyRecipeDto.Mine> result = PageableExecutionUtils.getPage(content, PageRequest.of(0, size), () -> 33);
        willReturn(result).given(recipeService).findMyRecipes(memberId, page, Enum.valueOf(RecipeSort.class, sort));

        // expected
        ResultActions actions = mockMvc.perform(get("/api/recipes/my")
                        .param("page", Integer.toString(page))
                        .param("sort", sort)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray())
                .andDo(MockMvcResultHandlers.print());

        //then
        actions.andDo(document("recipe-mine",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                requestParameters(
                        parameterWithName("page").description("요청 페이지 번호"),
                        parameterWithName("sort").description("정렬 기준. (VIEW 조회수, RECENT 최신순, HEART 하트 많은 순)")
                ),
                responseFields(
                        fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("레시피 리스트"),
                        fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("검색된 레시피의 ID"),
                        fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("검색된 레시피의 제목"),
                        fieldWithPath("data.[].imagePath").type(JsonFieldType.STRING).description("레시피의 대표 이미지 경로"),
                        fieldWithPath("data.[].view").type(JsonFieldType.NUMBER).description("조회수"),
                        fieldWithPath("data.[].heartCounts").type(JsonFieldType.NUMBER).description("레시피의 하트 수"),
                        fieldWithPath("data.[].heartExist").type(JsonFieldType.BOOLEAN).description("나의 해당 레시피 하트 여부 (미로그인시 false)"),
                        fieldWithPath("data.[].commentCounts").type(JsonFieldType.NUMBER).description("레시피 댓글 갯수"),
                        fieldWithPath("data.[].lastModifiedAt").type(JsonFieldType.STRING).description("레시피 마지막 수정 일자"),
                        fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                        fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("현재 사이즈"),
                        fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("레시피 전체 수"),
                        fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 개수")
                )
        ));
    }

    @Test
    @DisplayName("내가 하트를 추가한 레시피 리스트를 하트를 추가한 날짜 내림차순 검색")
    void getFavoriteRecipesTest() throws Exception {
        //given
        int page = 1;
        int size = 4;
        Long memberId = memberRepository.findAll().get(0).getId();

        List<MyRecipeDto.Favorite> content = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            MyRecipeDto.Favorite dto = new MyRecipeDto.Favorite((long) i, "Tomato pasta " + i, "https://s3.aws.abcdefg/recipe" + i + ".jpeg", (long) i*10, "Member"+i, "https://s3.aws.abcdefg/member" + i + ".jpeg", LocalDateTime.now());
            content.add(dto);
        }
        Page<MyRecipeDto.Favorite> result = PageableExecutionUtils.getPage(content, PageRequest.of(0, size), () -> 33);
        willReturn(result).given(recipeService).findFavoriteRecipes(memberId, page);

        // expected
        ResultActions actions = mockMvc.perform(get("/api/recipes/favorite")
                        .param("page", Integer.toString(page))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray())
                .andDo(MockMvcResultHandlers.print());

        //then
        actions.andDo(document("recipe-favorite",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                requestParameters(
                        parameterWithName("page").description("요청 페이지 번호")
                ),
                responseFields(
                        fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("레시피 리스트"),
                        fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("검색된 레시피의 ID"),
                        fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("검색된 레시피의 제목"),
                        fieldWithPath("data.[].imagePath").type(JsonFieldType.STRING).description("레시피의 대표 이미지 경로"),
                        fieldWithPath("data.[].member.id").type(JsonFieldType.NUMBER).description("레시피 작성자 ID"),
                        fieldWithPath("data.[].member.name").type(JsonFieldType.STRING).description("레시피 작성자 이름"),
                        fieldWithPath("data.[].member.profileImagePath").type(JsonFieldType.STRING).description("레시피 작성자 프로필 이미지 경로").optional(),
                        fieldWithPath("data.[].lastModifiedAt").type(JsonFieldType.STRING).description("레시피 마지막 수정 일자"),
                        fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                        fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("현재 사이즈"),
                        fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("레시피 전체 수"),
                        fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 개수")
                )
        ));
    }

    @Test
    void 메인페이지_최신순추천_테스트() throws Exception {
        //given
        List<RecipeRecommendDto> result = IntStream.range(1, 9)
                .mapToObj(i -> new RecipeRecommendDto((long) i, "title " + i, "https://s3.aws.abcdefg/recipe" + i + ".jpeg"))
                .collect(Collectors.toList());
        willReturn(result).given(recipeService).findRecentRecipes();

        //when
        ResultActions actions = mockMvc.perform(get("/api/recipes/recommend/recent")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray());

        //then
        actions.andDo(document("recent-recipe-recommend",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                responseFields(
                        fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("레시피 리스트"),
                        fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("검색된 레시피의 ID"),
                        fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("검색된 레시피의 제목"),
                        fieldWithPath("data.[].imagePath").type(JsonFieldType.STRING).description("레시피의 대표 이미지 경로")
                )));
    }

    @Test
    void 메인페이지_인기순추천_테스트() throws Exception {
        //given
        List<RecipeRecommendDto> result = IntStream.range(1, 9)
                .mapToObj(i -> new RecipeRecommendDto((long) i, "title " + i, "https://s3.aws.abcdefg/recipe" + i + ".jpeg"))
                .collect(Collectors.toList());
        willReturn(result).given(recipeService).findPopularRecipes();

        //when
        ResultActions actions = mockMvc.perform(get("/api/recipes/recommend/popular")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray());

        //then
        actions.andDo(document("popular-recipe-recommend",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                responseFields(
                        fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("레시피 리스트"),
                        fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("검색된 레시피의 ID"),
                        fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("검색된 레시피의 제목"),
                        fieldWithPath("data.[].imagePath").type(JsonFieldType.STRING).description("레시피의 대표 이미지 경로")
                )));
    }

    @Test
    @DisplayName("냉장고 재료를 기준으로 레시피 추천")
    void getRecommendRecipesByFridgeTest() throws Exception {
        // given
        Fridge fridge = new Fridge();
        ReflectionTestUtils.setField(fridge,"id",1L);
        willReturn(fridge).given(fridgeService).findFridge(anyLong());

        List<RecipeRecommendDto> result = IntStream.range(1, 9)
                .mapToObj(i -> new RecipeRecommendDto((long) i, "title " + i, "https://s3.aws.abcdefg/recipe" + i + ".jpeg"))
                .collect(Collectors.toList());
        willReturn(result).given(recipeService).recommendByFridge(anyLong());

        // expected
        ResultActions actions = mockMvc.perform(get("/api/recipes/recommend/fridge")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray())
                .andDo(MockMvcResultHandlers.print());

        // docs
        actions.andDo(document("recipe-recommend-fridge",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                responseFields(
                        fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("레시피 리스트"),
                        fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("검색된 레시피의 ID"),
                        fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("검색된 레시피의 제목"),
                        fieldWithPath("data.[].imagePath").type(JsonFieldType.STRING).description("레시피의 대표 이미지 경로")
                )
        ));
    }
}