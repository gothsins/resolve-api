package com.gothsins.resolve.controller;

import com.gothsins.resolve.dto.LoginRequestDTO;
import com.gothsins.resolve.dto.LoginResponseDTO;
import com.gothsins.resolve.dto.UserRequestDTO;
import com.gothsins.resolve.dto.UserResponseDTO;
import com.gothsins.resolve.security.JwtService;
import com.gothsins.resolve.service.RateLimitService;
import com.gothsins.resolve.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para registro e login de usuários")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final RateLimitService rateLimitService;

    @PostMapping("/register")
    @Operation(
            summary = "Registrar um novo usuário",
            description = "Cria uma nova conta de usuário na plataforma. Limite de 5 registros por IP por hora."
    )
    @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de registro inválidos (ex: senha muito curta, email malformado)")
    @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    @ApiResponse(responseCode = "429", description = "Limite de registro de usuários atingido para este IP")
    public ResponseEntity<UserResponseDTO> register(
            @Valid @RequestBody UserRequestDTO dto,
            HttpServletRequest request) {

        String ip = request.getRemoteAddr();

        if (!rateLimitService.allowUserCreation(ip)) {
            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .build();
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.create(dto));
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login de usuário",
            description = "Autentica o usuário e retorna um token JWT válido por 1 hora."
    )
    @ApiResponse(responseCode = "200", description = "Login bem-sucedido, token JWT retornado")
    @ApiResponse(responseCode = "400", description = "Corpo da requisição inválido (email ou senha ausentes)")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(LoginResponseDTO.builder().token(token).build());
    }
}