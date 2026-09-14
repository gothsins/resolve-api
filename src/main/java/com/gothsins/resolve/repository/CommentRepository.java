package com.gothsins.resolve.repository;

import com.gothsins.resolve.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    boolean existsByTicketIdAndAuthorIdAndContentAndCreatedAtAfter(
            Long ticketId, Long authorId, String content, LocalDateTime after);}
