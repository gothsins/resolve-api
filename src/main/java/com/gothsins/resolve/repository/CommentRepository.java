package com.gothsins.resolve.repository;

import com.gothsins.resolve.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
