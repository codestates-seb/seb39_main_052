package com.seb39.myfridge.comment.repository;

import com.seb39.myfridge.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
