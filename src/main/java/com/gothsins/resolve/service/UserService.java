package com.gothsins.resolve.service;

import com.gothsins.resolve.dto.UserRequestDTO;
import com.gothsins.resolve.dto.UserResponseDTO;
import com.gothsins.resolve.entity.User;
import com.gothsins.resolve.entity.enums.Role;
import com.gothsins.resolve.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponseDTO create(UserRequestDTO dto) {
        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(Role.USER)
                .active(true)
                .build();

        User saved = userRepository.save(user);

        return toResponseDTO(saved);
    }

    private UserResponseDTO toResponseDTO(User user) {
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
