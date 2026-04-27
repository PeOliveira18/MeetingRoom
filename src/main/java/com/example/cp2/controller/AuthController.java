package com.example.cp2.controller;

import com.example.cp2.dto.LoginDTO;
import com.example.cp2.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    @Operation(summary = "Realiza login e retorna token JWT")
    public ResponseEntity<LoginDTO.Response> login(@Valid @RequestBody LoginDTO.Request dto) {
        return ResponseEntity.ok(service.login(dto));
    }
}
