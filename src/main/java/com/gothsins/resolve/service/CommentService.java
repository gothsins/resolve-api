package com.gothsins.resolve.service;

import com.gothsins.resolve.dto.CommentRequestDTO;
import com.gothsins.resolve.dto.CommentResponseDTO;
import com.gothsins.resolve.dto.UserResponseDTO;
import com.gothsins.resolve.entity.Comment;
import com.gothsins.resolve.entity.Ticket;
import com.gothsins.resolve.entity.User;
import com.gothsins.resolve.exception.ResourceNotFoundException;
import com.gothsins.resolve.repository.CommentRepository;
import com.gothsins.resolve.repository.TicketRepository;
import com.gothsins.resolve.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponseDTO create(CommentRequestDTO dto) {
        Ticket ticket = ticketRepository.findById(dto.getTicketId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ticket não encontrado: id " + dto.getTicketId()));

        User author = userRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário (author) não encontrado: id " + dto.getAuthorId()));

        Comment comment = Comment.builder()
                .content(dto.getContent())
                .ticket(ticket)
                .author(author)
                .build();

        Comment saved = commentRepository.save(comment);
        return toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public CommentResponseDTO findById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Comentário não encontrado: id " + id));
        return toResponseDTO(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDTO> findAll() {
        return commentRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Comentário não encontrado: id " + id));
        commentRepository.delete(comment);
    }

    private CommentResponseDTO toResponseDTO(Comment comment) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .ticketId(comment.getTicket().getId())
                .author(toUserDTO(comment.getAuthor()))
                .createdAt(comment.getCreatedAt())
                .build();
    }

    private UserResponseDTO toUserDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}