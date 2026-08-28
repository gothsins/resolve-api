package com.gothsins.resolve.controller;

import com.gothsins.resolve.dto.ChangePasswordDTO;
import com.gothsins.resolve.dto.UserRequestDTO;
import com.gothsins.resolve.dto.UserResponseDTO;
import com.gothsins.resolve.service.RateLimitService;
import com.gothsins.resolve.service.UserService;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RateLimitService rateLimitService;


    @PostMapping
    public ResponseEntity<UserResponseDTO> create(
            @Valid @RequestBody UserRequestDTO dto,
            HttpServletRequest request) {

        String ip = request.getRemoteAddr();

        if (!rateLimitService.allowUserCreation(ip)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .build();
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {return ResponseEntity.ok(userService.findAll());}

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(userService.update(id, dto));
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<UserResponseDTO> updatePassword(@PathVariable Long id, @Valid @RequestBody ChangePasswordDTO dto) {
        return ResponseEntity.ok(userService.updatePassword(id, dto.getCurrentPassword(), dto.getNewPassword()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        userService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
