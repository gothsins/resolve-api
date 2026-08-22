package com.gothsins.resolve.controller;

import com.gothsins.resolve.dto.ChangePasswordDTO;
import com.gothsins.resolve.dto.UserRequestDTO;
import com.gothsins.resolve.dto.UserResponseDTO;
import com.gothsins.resolve.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.status(201).body(userService.create(dto));
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
