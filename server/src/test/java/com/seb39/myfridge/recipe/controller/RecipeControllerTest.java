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
    void ???????????????_??????() {
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
    public void ???????????????_?????????() throws Exception {
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

        MockMultipartFile image = new MockMultipartFile("files", "???????????? ?????? ?????????", "image/png", new FileInputStream("src/test/resources/image/puppy.jpeg"));

        RecipeDto.Step step1 = RecipeDto.Step.builder()
                .sequence(1)
                .content("?????? ?????????")
                .imageInfo(imageInfo1)
                .build();
        stepList.add(step1);

        RecipeDto.Step step2 = RecipeDto.Step.builder()
                .sequence(2)
                .content("?????? ?????? ????????? ?????????.")
                .imageInfo(imageInfo2)
                .build();
        stepList.add(step2);

        RecipeDto.Step step3 = RecipeDto.Step.builder()
                .sequence(3)
                .content("?????? ?????????.")
                .imageInfo(imageInfo3)
                .build();
        stepList.add(step3);

        RecipeDto.Ingredient ingredient1 = RecipeDto.Ingredient.builder()
                .name("????????????")
                .quantity("2??????")
                .build();

        RecipeDto.Ingredient ingredient2 = RecipeDto.Ingredient.builder()
                .name("??????")
                .quantity("10g")
                .build();

        List<RecipeDto.Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);


        RecipeDto.Post requestBody = new RecipeDto.Post("?????? ????????? ????????? ???", 1, "5???", stepList, ingredients);


        Member member = memberRepository.findGeneralByEmail("test@email.com").get();

        RecipeDto.ResponseDetail response = RecipeDto.ResponseDetail.builder()
                .id(1L)
                .title("?????? ????????? ????????? ???")
                .portion(1)
                .view(0)
                .time("5???")
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
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("????????? ??????"),
                                        fieldWithPath("portion").type(JsonFieldType.NUMBER).description("?????? ???????????? ??? ????????????"),
                                        fieldWithPath("view").type(JsonFieldType.NUMBER).description("?????????"),
                                        fieldWithPath("time").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("????????? ?????????"),
                                        fieldWithPath("lastModifiedAt").type(JsonFieldType.STRING).description("????????? ?????????"),
                                        fieldWithPath("imageInfo").type(JsonFieldType.OBJECT).description("????????? ?????? ????????? ??????"),
                                        fieldWithPath("imageInfo.idx").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                        fieldWithPath("imageInfo.imagePath").type(JsonFieldType.STRING).description("????????? Path"),
                                        fieldWithPath("imageInfo.isUpdated").type(JsonFieldType.STRING).description("????????? ?????? ??????"),
                                        fieldWithPath("ingredients").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                        fieldWithPath("ingredients.[].name").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                        fieldWithPath("ingredients.[].quantity").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                        fieldWithPath("steps").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                        fieldWithPath("steps.[].sequence").type(JsonFieldType.NUMBER).description("?????? ?????? ??????"),
                                        fieldWithPath("steps.[].content").type(JsonFieldType.STRING).description("??? ????????? ??????"),
                                        fieldWithPath("steps.[].imageInfo").type(JsonFieldType.OBJECT).description("?????? ????????? ????????? ??????"),
                                        fieldWithPath("steps.[].imageInfo.idx").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                        fieldWithPath("steps.[].imageInfo.imagePath").type(JsonFieldType.STRING).description("????????? Path"),
                                        fieldWithPath("steps.[].imageInfo.isUpdated").type(JsonFieldType.STRING).description("????????? ?????? ??????"),
                                        fieldWithPath("member.id").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                        fieldWithPath("member.name").type(JsonFieldType.STRING).description("????????? ??????"),
                                        fieldWithPath("member.profileImagePath").type(JsonFieldType.STRING).description("????????? ????????? ????????? ??????").optional(),
                                        fieldWithPath("heartCounts").type(JsonFieldType.NUMBER).description("?????? ?????? ??????"),
                                        fieldWithPath("heartExist").type(JsonFieldType.BOOLEAN).description("(????????? ???????????? ??????) ????????? ????????? ???????????? ??????")
                                )
                        )
                ));
    }

    @Test
    public void ???????????????_?????????() throws Exception {
        //given
        List<RecipeDto.Step> stepList = new ArrayList<>();

        MockMultipartFile image = new MockMultipartFile("files", "???????????? ?????? ?????????", "image/png", new FileInputStream("src/test/resources/image/puppy.jpeg"));

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
                .content("?????? ?????????")
                .imageInfo(imageInfo1)
                .build();
        stepList.add(step1);

        RecipeDto.Step step2 = RecipeDto.Step.builder()
                .sequence(2)
                .content("?????? ?????? ????????? ?????????.")
                .imageInfo(imageInfo2)
                .build();
        stepList.add(step2);

        RecipeDto.Step step3 = RecipeDto.Step.builder()
                .sequence(3)
                .content("?????? ?????????.")
                .imageInfo(imageInfo3)
                .build();
        stepList.add(step3);

        RecipeDto.Ingredient ingredient1 = RecipeDto.Ingredient.builder()
                .name("????????????")
                .quantity("2??????")
                .build();

        RecipeDto.Ingredient ingredient2 = RecipeDto.Ingredient.builder()
                .name("??????")
                .quantity("10g")
                .build();

        List<RecipeDto.Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);

        RecipeDto.Post requestBody = new RecipeDto.Post("?????? ????????? ????????? ???", 1, "5???", stepList, ingredients);

        String requestToJson = objectMapper.writeValueAsString(requestBody);
        MockMultipartFile json = new MockMultipartFile("requestBody", "requestToJson", "application/json", requestToJson.getBytes(StandardCharsets.UTF_8));

        RecipeDto.Patch patch = RecipeDto.Patch.builder()
                .id(1L)
                .title("?????? ??????????????? ????????? ???!")
                .portion(1)
                .time("3???")
                .steps(stepList)
                .imageInfo(imageInfo0)
                .build();

        Member member = memberRepository.findGeneralByEmail("test@email.com").get();

        RecipeDto.ResponseDetail response = RecipeDto.ResponseDetail.builder()
                .id(1L)
                .title("?????? ????????? ????????? ???")
                .portion(1)
                .view(0)
                .time("5???")
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
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("????????? ??????"),
                                        fieldWithPath("portion").type(JsonFieldType.NUMBER).description("?????? ???????????? ??? ????????????"),
                                        fieldWithPath("view").type(JsonFieldType.NUMBER).description("?????????"),
                                        fieldWithPath("time").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("????????? ?????????"),
                                        fieldWithPath("lastModifiedAt").type(JsonFieldType.STRING).description("????????? ?????????"),
                                        fieldWithPath("imageInfo").type(JsonFieldType.OBJECT).description("????????? ?????? ????????? ??????"),
                                        fieldWithPath("imageInfo.idx").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                        fieldWithPath("imageInfo.imagePath").type(JsonFieldType.STRING).description("????????? Path"),
                                        fieldWithPath("imageInfo.isUpdated").type(JsonFieldType.STRING).description("????????? ?????? ??????"),
                                        fieldWithPath("ingredients").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                        fieldWithPath("ingredients.[].name").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                        fieldWithPath("ingredients.[].quantity").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                        fieldWithPath("steps").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                        fieldWithPath("steps.[].sequence").type(JsonFieldType.NUMBER).description("?????? ?????? ??????"),
                                        fieldWithPath("steps.[].content").type(JsonFieldType.STRING).description("??? ????????? ??????"),
                                        fieldWithPath("steps.[].imageInfo").type(JsonFieldType.OBJECT).description("?????? ????????? ????????? ??????"),
                                        fieldWithPath("steps.[].imageInfo.idx").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                        fieldWithPath("steps.[].imageInfo.imagePath").type(JsonFieldType.STRING).description("????????? Path"),
                                        fieldWithPath("steps.[].imageInfo.isUpdated").type(JsonFieldType.STRING).description("????????? ?????? ??????"),
                                        fieldWithPath("member.id").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                        fieldWithPath("member.name").type(JsonFieldType.STRING).description("????????? ??????"),
                                        fieldWithPath("member.profileImagePath").type(JsonFieldType.STRING).description("????????? ????????? ????????? ??????").optional(),
                                        fieldWithPath("heartCounts").type(JsonFieldType.NUMBER).description("?????? ?????? ??????"),
                                        fieldWithPath("heartExist").type(JsonFieldType.BOOLEAN).description("(????????? ???????????? ??????) ????????? ????????? ???????????? ??????")
                                )
                        )
                ));
    }

    @Test
    public void ???????????????_?????????() throws Exception {
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
                                parameterWithName("id").description("????????? ????????? ?????????")
                        )));
    }

    @Test
    @DisplayName("????????? ?????? ?????? ?????????")
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
        recipe.setTime("30???");
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
                                parameterWithName("id").description("????????? ???????????? ID")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("????????? ??????"),
                                        fieldWithPath("portion").type(JsonFieldType.NUMBER).description("?????? ???????????? ??? ????????????"),
                                        fieldWithPath("view").type(JsonFieldType.NUMBER).description("?????????"),
                                        fieldWithPath("time").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("????????? ?????????"),
                                        fieldWithPath("lastModifiedAt").type(JsonFieldType.STRING).description("????????? ?????????"),
                                        fieldWithPath("imageInfo").type(JsonFieldType.OBJECT).description("????????? ?????? ????????? ??????"),
                                        fieldWithPath("imageInfo.idx").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                        fieldWithPath("imageInfo.imagePath").type(JsonFieldType.STRING).description("????????? Path"),
                                        fieldWithPath("imageInfo.isUpdated").type(JsonFieldType.STRING).description("????????? ?????? ??????"),
                                        fieldWithPath("ingredients").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                        fieldWithPath("ingredients.[].name").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                        fieldWithPath("ingredients.[].quantity").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                        fieldWithPath("steps").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                        fieldWithPath("steps.[].sequence").type(JsonFieldType.NUMBER).description("?????? ?????? ??????"),
                                        fieldWithPath("steps.[].content").type(JsonFieldType.STRING).description("??? ????????? ??????"),
                                        fieldWithPath("steps.[].imageInfo").type(JsonFieldType.OBJECT).description("?????? ????????? ????????? ??????"),
                                        fieldWithPath("steps.[].imageInfo.idx").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                        fieldWithPath("steps.[].imageInfo.imagePath").type(JsonFieldType.STRING).description("????????? Path"),
                                        fieldWithPath("steps.[].imageInfo.isUpdated").type(JsonFieldType.STRING).description("????????? ?????? ??????"),
                                        fieldWithPath("member.id").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                        fieldWithPath("member.name").type(JsonFieldType.STRING).description("????????? ??????"),
                                        fieldWithPath("member.profileImagePath").type(JsonFieldType.STRING).description("????????? ????????? ????????? ??????").optional(),
                                        fieldWithPath("heartCounts").type(JsonFieldType.NUMBER).description("?????? ?????? ??????"),
                                        fieldWithPath("heartExist").type(JsonFieldType.BOOLEAN).description("(????????? ???????????? ??????) ????????? ????????? ???????????? ??????")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("?????? ????????? ????????? ????????? ?????? ??????")
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
                                parameterWithName("word").description("?????? ???????????? ??????")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("????????? ?????? ?????????")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("????????? ????????? ????????? ????????? ??????")
    void searchRecipesTest() throws Exception {
        //given
        RecipeSearch.Request request = new RecipeSearch.Request();
        request.setTitle("??????");
        request.setPage(1);
        request.setIngredients(List.of("??????", "??????"));
        request.setSort(RecipeSort.VIEW);
        String requestJson = objectMapper.writeValueAsString(request);

        List<RecipeSearch.Response> content = new ArrayList<>();
        for (int i = 1; i <= 16; i++) {
            RecipeSearch.Response dto = new RecipeSearch.Response((long) i, "???????????? " + i, (long) i, "member" + i, "https://s3.aws.abcd/member" + i + ".jpeg", "https://s3.aws.abcd/recipe" + i + ".jpeg", 10, 17 - i, LocalDateTime.now());
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
                        fieldWithPath("title").type(JsonFieldType.STRING).description("????????? ????????? ??????"),
                        fieldWithPath("ingredients").type(JsonFieldType.ARRAY).description("????????? ?????? ?????? ?????????"),
                        fieldWithPath("page").type(JsonFieldType.NUMBER).description("????????? ????????? ??????"),
                        fieldWithPath("sort").type(JsonFieldType.STRING).description("???????????? ?????? ??????. (VIEW ?????????, RECENT ?????????, HEART ?????? ?????? ???)")
                ),
                responseFields(
                        fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("????????? ?????????"),
                        fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("????????? ???????????? ID"),
                        fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("????????? ???????????? ??????"),
                        fieldWithPath("data.[].member.id").type(JsonFieldType.NUMBER).description("????????? ????????? ID"),
                        fieldWithPath("data.[].member.name").type(JsonFieldType.STRING).description("????????? ????????? ??????"),
                        fieldWithPath("data.[].member.profileImagePath").type(JsonFieldType.STRING).description("????????? ????????? ????????? ????????? ??????").optional(),
                        fieldWithPath("data.[].imagePath").type(JsonFieldType.STRING).description("???????????? ?????? ????????? ??????"),
                        fieldWithPath("data.[].heartCounts").type(JsonFieldType.NUMBER).description("???????????? ?????? ???"),
                        fieldWithPath("data.[].view").type(JsonFieldType.NUMBER).description("?????????"),
                        fieldWithPath("data.[].lastModifiedAt").type(JsonFieldType.STRING).description("????????? ????????? ?????? ??????"),
                        fieldWithPath("data.[].heartExist").type(JsonFieldType.BOOLEAN).description("?????? ?????? ????????? ?????? ?????? (??????????????? false)"),
                        fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                        fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                        fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("????????? ?????? ???"),
                        fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("??? ????????? ??????")
                )
        ));
    }

    @Test
    @DisplayName("?????? ????????? ????????? ????????? ??????")
    void getMyRecipesTest() throws Exception {
        //given
        int page = 1;
        int size = 4;
        String sort = "RECENT";
        Long memberId = memberRepository.findAll().get(0).getId();

        List<MyRecipeDto.Mine> content = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            MyRecipeDto.Mine dto = new MyRecipeDto.Mine((long) i, "????????? " + i, "https://s3.aws.abcdefg/recipe" + i + ".jpeg", 1234, 10 - i, 5 - i, LocalDateTime.now());
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
                        parameterWithName("page").description("?????? ????????? ??????"),
                        parameterWithName("sort").description("?????? ??????. (VIEW ?????????, RECENT ?????????, HEART ?????? ?????? ???)")
                ),
                responseFields(
                        fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("????????? ?????????"),
                        fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("????????? ???????????? ID"),
                        fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("????????? ???????????? ??????"),
                        fieldWithPath("data.[].imagePath").type(JsonFieldType.STRING).description("???????????? ?????? ????????? ??????"),
                        fieldWithPath("data.[].view").type(JsonFieldType.NUMBER).description("?????????"),
                        fieldWithPath("data.[].heartCounts").type(JsonFieldType.NUMBER).description("???????????? ?????? ???"),
                        fieldWithPath("data.[].heartExist").type(JsonFieldType.BOOLEAN).description("?????? ?????? ????????? ?????? ?????? (??????????????? false)"),
                        fieldWithPath("data.[].commentCounts").type(JsonFieldType.NUMBER).description("????????? ?????? ??????"),
                        fieldWithPath("data.[].lastModifiedAt").type(JsonFieldType.STRING).description("????????? ????????? ?????? ??????"),
                        fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                        fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                        fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("????????? ?????? ???"),
                        fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("??? ????????? ??????")
                )
        ));
    }

    @Test
    @DisplayName("?????? ????????? ????????? ????????? ???????????? ????????? ????????? ?????? ???????????? ??????")
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
                        parameterWithName("page").description("?????? ????????? ??????")
                ),
                responseFields(
                        fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("????????? ?????????"),
                        fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("????????? ???????????? ID"),
                        fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("????????? ???????????? ??????"),
                        fieldWithPath("data.[].imagePath").type(JsonFieldType.STRING).description("???????????? ?????? ????????? ??????"),
                        fieldWithPath("data.[].member.id").type(JsonFieldType.NUMBER).description("????????? ????????? ID"),
                        fieldWithPath("data.[].member.name").type(JsonFieldType.STRING).description("????????? ????????? ??????"),
                        fieldWithPath("data.[].member.profileImagePath").type(JsonFieldType.STRING).description("????????? ????????? ????????? ????????? ??????").optional(),
                        fieldWithPath("data.[].lastModifiedAt").type(JsonFieldType.STRING).description("????????? ????????? ?????? ??????"),
                        fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                        fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                        fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("????????? ?????? ???"),
                        fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("??? ????????? ??????")
                )
        ));
    }

    @Test
    void ???????????????_???????????????_?????????() throws Exception {
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
                        fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("????????? ?????????"),
                        fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("????????? ???????????? ID"),
                        fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("????????? ???????????? ??????"),
                        fieldWithPath("data.[].imagePath").type(JsonFieldType.STRING).description("???????????? ?????? ????????? ??????")
                )));
    }

    @Test
    void ???????????????_???????????????_?????????() throws Exception {
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
                        fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("????????? ?????????"),
                        fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("????????? ???????????? ID"),
                        fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("????????? ???????????? ??????"),
                        fieldWithPath("data.[].imagePath").type(JsonFieldType.STRING).description("???????????? ?????? ????????? ??????")
                )));
    }

    @Test
    @DisplayName("????????? ????????? ???????????? ????????? ??????")
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
                        fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("????????? ?????????"),
                        fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("????????? ???????????? ID"),
                        fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("????????? ???????????? ??????"),
                        fieldWithPath("data.[].imagePath").type(JsonFieldType.STRING).description("???????????? ?????? ????????? ??????")
                )
        ));
    }
}