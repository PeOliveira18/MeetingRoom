package com.example.cp2.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

public class SalaDTO {

    @Data
    public static class Request {
        @NotBlank(message = "Nome é obrigatório")
        private String nome;

        @NotNull(message = "Capacidade é obrigatória")
        @Min(value = 1, message = "Capacidade mínima é 1")
        private Integer capacidade;

        @NotBlank(message = "Localização é obrigatória")
        private String localizacao;
    }

    @Data
    public static class Response {
        private Long id;
        private String nome;
        private Integer capacidade;
        private String localizacao;
    }
}
