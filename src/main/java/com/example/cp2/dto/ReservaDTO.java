package com.example.cp2.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

public class ReservaDTO {

    @Data
    public static class Request {
        @NotNull(message = "ID da sala é obrigatório")
        private Long salaId;

        @NotNull(message = "Data/hora de início é obrigatória")
        @Future(message = "Data de início deve ser futura")
        private LocalDateTime dataHoraInicio;

        @NotNull(message = "Data/hora de fim é obrigatória")
        private LocalDateTime dataHoraFim;

        @NotBlank(message = "Responsável é obrigatório")
        private String responsavel;
    }

    @Data
    public static class Response {
        private Long id;
        private Long salaId;
        private String nomeSala;
        private LocalDateTime dataHoraInicio;
        private LocalDateTime dataHoraFim;
        private String responsavel;
        private String status;
    }
}