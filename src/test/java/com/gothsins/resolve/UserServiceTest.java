package com.gothsins.resolve;

import com.gothsins.resolve.dto.UserResponseDTO;
import com.gothsins.resolve.entity.User;
import com.gothsins.resolve.exception.ResourceNotFoundException;
import com.gothsins.resolve.repository.UserRepository;
import com.gothsins.resolve.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

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

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("Guiverme", result.getName());
        assertEquals("guiverme@email.com", result.getEmail());

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
}