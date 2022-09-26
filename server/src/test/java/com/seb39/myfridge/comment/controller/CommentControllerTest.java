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
    @DisplayName("특정 레시피에 코멘트를 생성한다.")
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
                .andExpect(jsonPath("$.memberId").value(member.getId()))
                .andExpect(jsonPath("$.memberName").value(member.getName()))
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
                        parameterWithName("recipeId").description("댓글을 작성할 레시피 ID")
                ),
                requestFields(
                        fieldWithPath("content").type(JsonFieldType.STRING).description("작성한 댓글 내용")
                ),
                responseFields(
                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("작성자 ID"),
                        fieldWithPath("memberName").type(JsonFieldType.STRING).description("작성자 이름"),
                        fieldWithPath("recipeId").type(JsonFieldType.NUMBER).description("댓글이 달린 레시피 ID"),
                        fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("작성된 댓글 ID"),
                        fieldWithPath("content").type(JsonFieldType.STRING).description("작성된 댓글 내용"),
                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("작성된 댓글의 생성 일자")
                )
        ));
    }

    @Test
    @DisplayName("자신이 올린 코멘트를 수정한다.")
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
                .andExpect(jsonPath("$.memberId").value(member.getId()))
                .andExpect(jsonPath("$.memberName").value(member.getName()))
                .andExpect(jsonPath("$.recipeId").value(recipe.getId()))
                .andExpect(jsonPath("$.commentId").value(comment.getId()))
                .andExpect(jsonPath("$.content").value(dto.getContent()));

        assertThat(comment.getContent()).isEqualTo(newContent);

        // docs
        result.andDo(document("comment-update",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                pathParameters(
                        parameterWithName("commentId").description("수정할 댓글의 ID")
                ),
                requestFields(
                        fieldWithPath("content").type(JsonFieldType.STRING).description("수정한 댓글 내용")
                ),
                responseFields(
                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("작성자 ID"),
                        fieldWithPath("memberName").type(JsonFieldType.STRING).description("작성자 이름"),
                        fieldWithPath("recipeId").type(JsonFieldType.NUMBER).description("수정된 댓글이 달린 레시피 ID"),
                        fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("수정된 댓글 ID"),
                        fieldWithPath("content").type(JsonFieldType.STRING).description("수정된 댓글 내용"),
                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("수정된 댓글의 생성 일자")
                )
        ));
    }

    @Test
    @DisplayName("자신이 올린 코멘트를 삭제한다.")
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
                        parameterWithName("commentId").description("삭제할 댓글의 ID")
                )
        ));
    }

    @Test
    @DisplayName("자신이 작성한 레시피에 달린 댓글을 조회한다.")
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
                        parameterWithName("page").description("조회할 댓글 리스트의 페이지 번호")
                ),
                responseFields(
                        fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("댓글 리스트"),
                        fieldWithPath("data.[].memberId").type(JsonFieldType.NUMBER).description("댓글 작성자 ID"),
                        fieldWithPath("data.[].memberName").type(JsonFieldType.STRING).description("댓글 작성자 이름"),
                        fieldWithPath("data.[].recipeId").type(JsonFieldType.NUMBER).description("댓글이 달린 레시피 ID"),
                        fieldWithPath("data.[].commentId").type(JsonFieldType.NUMBER).description("작성된 댓글 ID"),
                        fieldWithPath("data.[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                        fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING).description("댓글 생성 일자"),
                        fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                        fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("현재 사이즈"),
                        fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("질문 전체 수"),
                        fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 개수")
                )
        ));
    }

    @Test
    @DisplayName("특정 레시피에 달린 댓글 리스트를 조회한다.")
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
        result.andDo(document("comments-received",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                pathParameters(
                        parameterWithName("recipeId").description("조회할 댓글 리스트의 레시피 ID")
                ),
                requestParameters(
                        parameterWithName("page").description("조회할 댓글 리스트의 페이지 번호")
                ),
                responseFields(
                        fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("댓글 리스트"),
                        fieldWithPath("data.[].memberId").type(JsonFieldType.NUMBER).description("댓글 작성자 ID"),
                        fieldWithPath("data.[].memberName").type(JsonFieldType.STRING).description("댓글 작성자 이름"),
                        fieldWithPath("data.[].recipeId").type(JsonFieldType.NUMBER).description("댓글이 달린 레시피 ID"),
                        fieldWithPath("data.[].commentId").type(JsonFieldType.NUMBER).description("작성된 댓글 ID"),
                        fieldWithPath("data.[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                        fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING).description("댓글 생성 일자"),
                        fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                        fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("현재 사이즈"),
                        fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("질문 전체 수"),
                        fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 개수")
                )
        ));
    }
}