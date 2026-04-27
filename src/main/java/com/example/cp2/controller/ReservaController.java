package com.example.cp2.controller;

import com.example.cp2.dto.ReservaDTO;
import com.example.cp2.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
@SecurityRequirement(name = "Bearer Auth")
@Tag(name = "Reservas")
public class ReservaController {

    private final ReservaService service;

    public ReservaController(ReservaService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar reserva")
    public ResponseEntity<ReservaDTO.Response> criar(@Valid @RequestBody ReservaDTO.Request dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto));
    }

    @GetMapping
    @Operation(summary = "Listar reservas com paginação e filtro")
    public ResponseEntity<Page<ReservaDTO.Response>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String responsavel) {
        return ResponseEntity.ok(service.listar(page, size, responsavel));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar reserva")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        service.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}