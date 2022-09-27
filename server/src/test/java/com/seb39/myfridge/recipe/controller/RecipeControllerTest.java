package com.seb39.myfridge.recipe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seb39.myfridge.image.entity.Image;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
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

        MockMultipartFile image = new MockMultipartFile("files","추가하고 싶은 이미지","image/png", new FileInputStream("src/test/resources/image/puppy.jpeg"));

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


        RecipeDto.Post requestBody = new RecipeDto.Post("라면 맛있게 끓이는 법",1,"5분", stepList, ingredients);


        Member member = memberRepository.findByEmail("test@email.com").get();

        RecipeDto.Response response = new RecipeDto.Response(
                1L,
                "라면 맛있게 끓이는 법",
                1,
                "5분",
                LocalDateTime.now(),
                LocalDateTime.now(),
                ingredients,
                stepList,
                member,
                imageInfo0
        );



        given(recipeMapper.recipePostToRecipe(any())).willReturn(new Recipe());
        given(recipeService.createRecipe(any(), anyList(), anyLong(), anyList(), anyList())).willReturn(new Recipe());
        given(recipeMapper.recipeToRecipeResponse(Mockito.any(Recipe.class))).willReturn(response);
        String requestToJson = objectMapper.writeValueAsString(requestBody);

        MockMultipartFile json = new MockMultipartFile("requestBody","jsonData","application/json", requestToJson.getBytes(StandardCharsets.UTF_8));

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
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("작성자 식별자"),
                                        fieldWithPath("memberName").type(JsonFieldType.STRING).description("작성자 이름")
                                )
                        )
                ));
    }
    @Test
    public void 레시피수정_테스트() throws Exception{
        //given
        List<RecipeDto.Step> stepList = new ArrayList<>();

        MockMultipartFile image = new MockMultipartFile("files","추가하고 싶은 이미지","image/png", new FileInputStream("src/test/resources/image/puppy.jpeg"));

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

        RecipeDto.Post requestBody = new RecipeDto.Post("라면 맛있게 끓이는 법",1,"5분", stepList,ingredients);
        String requestToJson = objectMapper.writeValueAsString(requestBody);
        MockMultipartFile json = new MockMultipartFile("requestBody","jsonData","application/json", requestToJson.getBytes(StandardCharsets.UTF_8));

        RecipeDto.Patch patch = RecipeDto.Patch.builder()
                .id(1L)
                .title("라면 백종원처럼 끓이는 법!")
                .imageInfo(imageInfo0)
                .build();

        Member member = memberRepository.findByEmail("test@email.com").get();

        RecipeDto.Response response = new RecipeDto.Response(
                1L,
                patch.getTitle(),
                1,
                "5분",
                LocalDateTime.now(),
                LocalDateTime.now(),
//                "https://seb52bucket.s3.ap-northeast-2.amazonaws.com/images/ffc76307-6043-437d-944f-ebc2bd2e0359.jpeg",
                ingredients,
                stepList,
                member,
                imageInfo0
        );
        given(recipeMapper.recipePatchToRecipe(Mockito.any(RecipeDto.Patch.class))).willReturn(new Recipe());
        given(recipeService.updateRecipe(any(), anyList(), anyLong(),anyList(), anyList())).willReturn(new Recipe());
        given(recipeMapper.recipeToRecipeResponse(Mockito.any(Recipe.class))).willReturn(response);

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
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("작성자 식별자"),
                                        fieldWithPath("memberName").type(JsonFieldType.STRING).description("작성자 이름")
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
}