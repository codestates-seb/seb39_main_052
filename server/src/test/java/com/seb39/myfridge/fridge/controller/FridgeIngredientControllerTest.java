package com.seb39.myfridge.fridge.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seb39.myfridge.fridge.dto.FridgeDto;
import com.seb39.myfridge.fridge.entity.Fridge;
import com.seb39.myfridge.fridge.entity.FridgeIngredient;
import com.seb39.myfridge.fridge.mapper.FridgeMapper;
import com.seb39.myfridge.fridge.service.FridgeIngredientService;
import com.seb39.myfridge.fridge.service.FridgeService;
import com.seb39.myfridge.ingredient.entity.Ingredient;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.repository.MemberRepository;
import com.seb39.myfridge.member.service.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static com.seb39.myfridge.util.ApiDocumentUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriHost = "seb39myfridge.ml", uriScheme = "https", uriPort = 443)
@WithUserDetails(value = "test@gmail.com", userDetailsServiceBeanName = "principalDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
class FridgeIngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FridgeMapper fridgeMapper;

    @Autowired
    private FridgeService fridgeService;

    @Autowired
    private FridgeIngredientService fridgeIngredientService;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void beforeEach() {
        Member member = Member.generalBuilder()
                .name("yongju")
                .email("test@gmail.com")
                .password("1234")
                .buildGeneralMember();
        memberService.signUpGeneral(member);
    }
    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
    }

   /* @Test
    void 냉장고재료조회_테스트() throws Exception {
        //given
        Member member = memberRepository.findByEmail("test@gmail.com").get();
        Fridge fridge = fridgeService.findFridge(member.getId());

        List<FridgeDto.IngredientInfo> infos = new ArrayList<>();
        List<FridgeIngredient> ingredients = new ArrayList<>();

        Ingredient ingredient = new Ingredient();
        ingredient.setName("돼지고기");
        FridgeIngredient fridgeIngredient1 = new FridgeIngredient();
        fridgeIngredient1.setFridge(fridge);
        fridgeIngredient1.setNote("구이용 돼지고기");
        fridgeIngredient1.setIngredient(ingredient);
        fridgeIngredient1.setQuantity("500g");
        fridgeIngredient1.setExpiration(LocalDate.of(2022,10,15));
        ingredients.add(fridgeIngredient1);
        FridgeDto.IngredientInfo ingredientInfo = fridgeMapper.fridgeIngredientToFridgeIngredientDto(fridgeIngredient1);
        infos.add(ingredientInfo);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("소고기");
        FridgeIngredient fridgeIngredient2 = new FridgeIngredient();
        fridgeIngredient2.setFridge(fridge);
        fridgeIngredient2.setNote("구이용 소고기");
        fridgeIngredient2.setIngredient(ingredient2);
        fridgeIngredient2.setQuantity("450g");
        fridgeIngredient2.setExpiration(LocalDate.of(2022,10,05));
        ingredients.add(fridgeIngredient2);
        FridgeDto.IngredientInfo ingredientInfo2 = fridgeMapper.fridgeIngredientToFridgeIngredientDto(fridgeIngredient2);
        infos.add(ingredientInfo2);

        given(fridgeIngredientService.findFridgeIngredient(any())).willReturn(ingredients);

        //when
        ResultActions perform = mockMvc.perform(get("/api/fridge").accept(MediaType.APPLICATION_JSON));
        //then
        perform.andExpect(status().isOk());
    }*/

    @Test
    void 냉장고재료입력_테스트() throws Exception {
        //given
        Member member = memberRepository.findByEmail("test@gmail.com").get();
        Fridge fridge = fridgeService.findFridge(member.getId());

        List<FridgeDto.IngredientInfo> infos = new ArrayList<>();
        List<FridgeIngredient> ingredients = new ArrayList<>();

        Ingredient ingredient = new Ingredient();
        ingredient.setName("돼지고기");
        FridgeIngredient fridgeIngredient1 = new FridgeIngredient();
        fridgeIngredient1.setFridge(fridge);
        fridgeIngredient1.setNote("구이용 돼지고기");
        fridgeIngredient1.setIngredient(ingredient);
        fridgeIngredient1.setQuantity("500g");
        fridgeIngredient1.setExpiration(LocalDate.of(2022,10,15));
        ingredients.add(fridgeIngredient1);
        FridgeDto.IngredientInfo ingredientInfo = fridgeMapper.fridgeIngredientToFridgeIngredientDto(fridgeIngredient1);
        infos.add(ingredientInfo);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("소고기");
        FridgeIngredient fridgeIngredient2 = new FridgeIngredient();
        fridgeIngredient2.setFridge(fridge);
        fridgeIngredient2.setNote("구이용 소고기");
        fridgeIngredient2.setIngredient(ingredient2);
        fridgeIngredient2.setQuantity("450g");
        fridgeIngredient2.setExpiration(LocalDate.of(2022,10,05));
        ingredients.add(fridgeIngredient2);
        FridgeDto.IngredientInfo ingredientInfo2 = fridgeMapper.fridgeIngredientToFridgeIngredientDto(fridgeIngredient2);
        infos.add(ingredientInfo2);


        FridgeDto.Post requestBody = FridgeDto.Post.builder()
                .fridgeIngredients(infos)
                .build();

        FridgeDto.Response response = fridgeMapper.fridgeIngredientsToResponse(ingredients);
        String requestJson = objectMapper.writeValueAsString(requestBody);
        //when
        ResultActions actions = mockMvc.perform(post("/api/fridge")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));
        //then
        actions.andExpect(status().isOk())
                .andDo(document("fridgeIngredients-create",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestFields(
                                List.of(
                                        fieldWithPath("fridgeIngredients").type(JsonFieldType.ARRAY).description("냉장고 속 재료들"),
                                        fieldWithPath("fridgeIngredients.[].name").type(JsonFieldType.STRING).description("냉장고에 추가할 재료 이름"),
                                        fieldWithPath("fridgeIngredients.[].quantity").type(JsonFieldType.STRING).description("냉장고에 추가할 재료 양"),
                                        fieldWithPath("fridgeIngredients.[].expiration").type(JsonFieldType.STRING).description("냉장고에 추가할 재료 유통기한"),
                                        fieldWithPath("fridgeIngredients.[].note").type(JsonFieldType.STRING).description("냉장고에 추가할 재료 관련 노트")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("fridgeIngredients").type(JsonFieldType.ARRAY).description("냉장고 속 재료들"),
                                        fieldWithPath("fridgeIngredients.[].id").type(JsonFieldType.NUMBER).description("냉장고 재료 식별자"),
                                        fieldWithPath("fridgeIngredients.[].name").type(JsonFieldType.STRING).description("냉장고에 추가할 재료 이름"),
                                        fieldWithPath("fridgeIngredients.[].quantity").type(JsonFieldType.STRING).description("냉장고에 추가할 재료 양"),
                                        fieldWithPath("fridgeIngredients.[].expiration").type(JsonFieldType.STRING).description("냉장고에 추가할 재료 유통기한"),
                                        fieldWithPath("fridgeIngredients.[].note").type(JsonFieldType.STRING).description("냉장고에 추가할 재료 관련 노트")

                                )
                        )
                ));
    }

}