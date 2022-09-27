package com.seb39.myfridge.comment.repository;

import com.seb39.myfridge.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "select c from Comment c join fetch c.recipe r join fetch r.member m where m.id = :memberId",
    countQuery = "select count(c) from Comment c join c.recipe r join r.member m where m.id = :memberId")
    Page<Comment> findAllByRecipeWriterId(@Param("memberId") Long recipeWriterId, Pageable pageable);

    @Query(value = "select c from Comment c join c.recipe where c.recipe.id = :recipeId")
    Page<Comment> findAllByRecipeId(@Param("recipeId") Long recipeId, Pageable pageable);

}
