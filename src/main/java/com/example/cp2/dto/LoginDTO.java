package com.example.cp2.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class LoginDTO {

    @Data
    public static class Request {
        @NotBlank
        private String username;

        @NotBlank
        private String password;
    }

    @Data
    public static class Response {
        private String token;
        private String username;
    }
}

