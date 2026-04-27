package com.example.cp2.controller;


import com.example.cp2.dto.SalaDTO;
import com.example.cp2.service.SalaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salas")
@SecurityRequirement(name = "Bearer Auth")
@Tag(name = "Salas de Reunião")
public class SalaController {

    private final SalaService service;

    public SalaController(SalaService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar sala")
    public ResponseEntity<SalaDTO.Response> criar(@Valid @RequestBody SalaDTO.Request dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto));
    }

    @GetMapping
    @Operation(summary = "Listar salas (com filtros opcionais)")
    public ResponseEntity<List<SalaDTO.Response>> listar(
            @RequestParam(required = false) String localizacao,
            @RequestParam(required = false) Integer capacidadeMinima) {
        return ResponseEntity.ok(service.listar(localizacao, capacidadeMinima));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sala por ID")
    public ResponseEntity<SalaDTO.Response> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar sala")
    public ResponseEntity<SalaDTO.Response> atualizar(@PathVariable Long id,
                                                     @Valid @RequestBody SalaDTO.Request dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover sala")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}