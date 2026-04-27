package com.example.cp2.service;

import com.example.cp2.dto.LoginDTO;
import com.example.cp2.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;
    private final String senhaHash;

    public AuthService(JwtUtil jwtUtil, PasswordEncoder encoder) {
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
        this.senhaHash = encoder.encode("admin123");
    }

    public LoginDTO.Response login(LoginDTO.Request dto) {
        log.info("Tentativa de login: usuario={}", dto.getUsername());

        if (!"admin".equals(dto.getUsername()) || !encoder.matches(dto.getPassword(), senhaHash)) {
            log.warn("Login falhou para usuario={}", dto.getUsername());
            throw new RuntimeException("Credenciais inválidas");
        }

        String token = jwtUtil.generateToken(dto.getUsername());
        log.info("Login bem-sucedido: usuario={}", dto.getUsername());

        LoginDTO.Response response = new LoginDTO.Response();
        response.setToken(token);
        response.setUsername(dto.getUsername());
        return response;
    }
}