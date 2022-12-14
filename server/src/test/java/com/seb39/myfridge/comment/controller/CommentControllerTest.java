package com.seb39.myfridge.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seb39.myfridge.comment.dto.CommentDto;
import com.seb39.myfridge.comment.entity.Comment;
import com.seb39.myfridge.comment.repository.CommentRepository;
import com.seb39.myfridge.comment.service.CommentService;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.repository.MemberRepository;
import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.recipe.repository.RecipeRepository;
import com.seb39.myfridge.recipe.service.RecipeService;
import com.seb39.myfridge.util.ApiDocumentUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.seb39.myfridge.util.ApiDocumentUtils.*;
import static org.assertj.core.api.Assertions.*;
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
class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentService commentService;

    @Autowired
    ObjectMapper om;

    @BeforeEach
    void beforeEach() {
        Member member = Member.generalBuilder()
                .name("SJ")
                .email("test@gmail.com")
                .buildGeneralMember();
        member.changeProfileImagePath("https://s3.blahblah/image.jpeg");
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
    @DisplayName("?????? ???????????? ???????????? ????????????.")
    void postComment() throws Exception {
        //given
        Member member = memberRepository.findAll().get(0);
        Recipe recipe = recipeRepository.findAll().get(0);

        CommentDto.Post dto = new CommentDto.Post();
        dto.setContent("comment1");
        String requestJson = om.writeValueAsString(dto);

        //expected
        ResultActions result = mockMvc.perform(post("/api/recipes/{recipeId}/comments", recipe.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.member.id").value(member.getId()))
                .andExpect(jsonPath("$.member.name").value(member.getName()))
                .andExpect(jsonPath("$.member.profileImagePath").value(member.getProfileImagePath()))
                .andExpect(jsonPath("$.recipeId").value(recipe.getId()))
                .andExpect(jsonPath("$.commentId").isNotEmpty())
                .andExpect(jsonPath("$.content").value(dto.getContent()));


        List<Comment> comments = commentRepository.findAll();
        assertThat(comments.size()).isEqualTo(1);
        Comment comment = comments.get(0);
        assertThat(comment.getMember()).isEqualTo(member);
        assertThat(comment.getRecipe()).isEqualTo(recipe);

        // docs
        result.andDo(document("comment-create",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                pathParameters(
                        parameterWithName("recipeId").description("????????? ????????? ????????? ID")
                ),
                requestFields(
                        fieldWithPath("content").type(JsonFieldType.STRING).description("????????? ?????? ??????")
                ),
                responseFields(
                        fieldWithPath("member.id").type(JsonFieldType.NUMBER).description("????????? ID"),
                        fieldWithPath("member.name").type(JsonFieldType.STRING).description("????????? ??????"),
                        fieldWithPath("member.profileImagePath").type(JsonFieldType.STRING).description("????????? ????????? ????????? ??????"),
                        fieldWithPath("recipeId").type(JsonFieldType.NUMBER).description("????????? ?????? ????????? ID"),
                        fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("????????? ?????? ID"),
                        fieldWithPath("content").type(JsonFieldType.STRING).description("????????? ?????? ??????"),
                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("????????? ????????? ?????? ??????")
                )
        ));
    }

    @Test
    @DisplayName("????????? ?????? ???????????? ????????????.")
    void patchComment() throws Exception {
        //given
        Member member = memberRepository.findAll().get(0);
        Recipe recipe = recipeRepository.findAll().get(0);
        Comment comment = commentService.writeComment("orignal comment", member.getId(), recipe.getId());

        CommentDto.Patch dto = new CommentDto.Patch();
        String newContent = "updated comment";
        dto.setContent(newContent);
        String requestJson = om.writeValueAsString(dto);

        //expected
        ResultActions result = mockMvc.perform(patch("/api/comments/{commentId}", comment.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.member.id").value(member.getId()))
                .andExpect(jsonPath("$.member.name").value(member.getName()))
                .andExpect(jsonPath("$.recipeId").value(recipe.getId()))
                .andExpect(jsonPath("$.commentId").value(comment.getId()))
                .andExpect(jsonPath("$.content").value(dto.getContent()));

        assertThat(comment.getContent()).isEqualTo(newContent);

        // docs
        result.andDo(document("comment-update",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                pathParameters(
                        parameterWithName("commentId").description("????????? ????????? ID")
                ),
                requestFields(
                        fieldWithPath("content").type(JsonFieldType.STRING).description("????????? ?????? ??????")
                ),
                responseFields(
                        fieldWithPath("member.id").type(JsonFieldType.NUMBER).description("????????? ID"),
                        fieldWithPath("member.name").type(JsonFieldType.STRING).description("????????? ??????"),
                        fieldWithPath("member.profileImagePath").type(JsonFieldType.STRING).description("????????? ????????? ????????? ??????"),
                        fieldWithPath("recipeId").type(JsonFieldType.NUMBER).description("????????? ????????? ?????? ????????? ID"),
                        fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("????????? ?????? ID"),
                        fieldWithPath("content").type(JsonFieldType.STRING).description("????????? ?????? ??????"),
                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("????????? ????????? ?????? ??????")
                )
        ));
    }

    @Test
    @DisplayName("????????? ?????? ???????????? ????????????.")
    void deleteComment() throws Exception {
        //given
        Member member = memberRepository.findAll().get(0);
        Recipe recipe = recipeRepository.findAll().get(0);
        Comment comment = commentService.writeComment("orignal comment", member.getId(), recipe.getId());

        //expected
        ResultActions result = mockMvc.perform(delete("/api/comments/{commentId}", comment.getId()))
                .andExpect(status().isNoContent());

        List<Comment> comments = commentRepository.findAll();
        assertThat(comments.size()).isEqualTo(0);

        // docs
        result.andDo(document("comment-delete",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                pathParameters(
                        parameterWithName("commentId").description("????????? ????????? ID")
                )
        ));
    }

    @Test
    @DisplayName("????????? ????????? ???????????? ?????? ????????? ????????????.")
    void getReceivedComments() throws Exception {
        //given
        Member recipeWriter = memberRepository.findAll().get(0);
        Recipe recipe = recipeRepository.findAll().get(0);

        for (int i = 1; i <= 30; i++) {
            Member commentWriter = Member.generalBuilder()
                    .name("Comment Writer " + i)
                    .buildGeneralMember();
            memberRepository.save(commentWriter);
            Comment comment = commentService.writeComment("orignal comment", commentWriter.getId(), recipe.getId());
        }

        //expected
        ResultActions result = mockMvc.perform(get("/api/comments/received")
                        .param("page","1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // docs
        result.andDo(document("comments-received",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                requestParameters(
                        parameterWithName("page").description("????????? ?????? ???????????? ????????? ??????")
                ),
                responseFields(
                        fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("?????? ?????????"),
                        fieldWithPath("data.[].member.id").type(JsonFieldType.NUMBER).description("?????? ????????? ID"),
                        fieldWithPath("data.[].member.name").type(JsonFieldType.STRING).description("?????? ????????? ??????"),
                        fieldWithPath("data.[].member.profileImagePath").type(JsonFieldType.STRING).description("?????? ????????? ????????? ????????? ??????").optional(),
                        fieldWithPath("data.[].recipeId").type(JsonFieldType.NUMBER).description("????????? ?????? ????????? ID"),
                        fieldWithPath("data.[].commentId").type(JsonFieldType.NUMBER).description("????????? ?????? ID"),
                        fieldWithPath("data.[].content").type(JsonFieldType.STRING).description("?????? ??????"),
                        fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                        fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                        fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                        fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("????????? ?????? ???"),
                        fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("??? ????????? ??????")
                )
        ));
    }

    @Test
    @DisplayName("?????? ???????????? ?????? ?????? ???????????? ????????????.")
    void getCommentsByRecipeId() throws Exception {
        //given
        Member recipeWriter = memberRepository.findAll().get(0);
        Recipe recipe = recipeRepository.findAll().get(0);

        for (int i = 1; i <= 24; i++) {
            Member commentWriter = Member.generalBuilder()
                    .name("Comment Writer " + i)
                    .buildGeneralMember();
            memberRepository.save(commentWriter);
            commentService.writeComment("comment " + i, commentWriter.getId(), recipe.getId());
        }

        //expected
        ResultActions result = mockMvc.perform(get("/api/recipes/{recipeId}/comments",recipe.getId())
                        .param("page","1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // docs
        result.andDo(document("comments-recipe",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                pathParameters(
                        parameterWithName("recipeId").description("????????? ?????? ???????????? ????????? ID")
                ),
                requestParameters(
                        parameterWithName("page").description("????????? ?????? ???????????? ????????? ??????")
                ),
                responseFields(
                        fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("?????? ?????????"),
                        fieldWithPath("data.[].member.id").type(JsonFieldType.NUMBER).description("?????? ????????? ID"),
                        fieldWithPath("data.[].member.name").type(JsonFieldType.STRING).description("?????? ????????? ??????"),
                        fieldWithPath("data.[].member.profileImagePath").type(JsonFieldType.STRING).description("?????? ????????? ????????? ????????? ??????").optional(),
                        fieldWithPath("data.[].recipeId").type(JsonFieldType.NUMBER).description("????????? ?????? ????????? ID"),
                        fieldWithPath("data.[].commentId").type(JsonFieldType.NUMBER).description("????????? ?????? ID"),
                        fieldWithPath("data.[].content").type(JsonFieldType.STRING).description("?????? ??????"),
                        fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                        fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                        fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                        fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("????????? ?????? ???"),
                        fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("??? ????????? ??????")
                )
        ));
    }
}