package com.gothsins.resolve;

import com.gothsins.resolve.dto.UserRequestDTO;
import com.gothsins.resolve.dto.UserResponseDTO;
import com.gothsins.resolve.entity.User;
import com.gothsins.resolve.exception.ResourceNotFoundException;
import com.gothsins.resolve.repository.UserRepository;
import com.gothsins.resolve.service.MetricsService;
import com.gothsins.resolve.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MetricsService metricsService;

    @InjectMocks
    private UserService userService;

    @Test
    void testGetUserById() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setName("Guiverme");
        user.setEmail("guiverme@email.com");

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        UserResponseDTO result = userService.findById(userId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(userId, result.getId());
        Assertions.assertEquals("Guiverme", result.getName());
        Assertions.assertEquals("guiverme@email.com", result.getEmail());

        verify(userRepository).findById(userId);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        Long userId = 999L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.findById(userId)
        );

        verify(userRepository).findById(userId);
    }

    @Test
    void testCreateUserRequestDTO() {

        UserRequestDTO dto = new UserRequestDTO();

        dto.setName("Guiverme");
        dto.setEmail("guiverme@email.com");
        dto.setPassword("p12345678");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName(dto.getName());
        savedUser.setEmail(dto.getEmail());
        savedUser.setPassword(dto.getPassword());

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(savedUser);

        UserResponseDTO result = userService.create(dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals("Guiverme", result.getName());
        Assertions.assertEquals("guiverme@email.com", result.getEmail());

        Mockito.verify(metricsService, Mockito.times(1)).incrementUserRegistered();
    }

    @Test
    void testCreateEmail(){


    }
}