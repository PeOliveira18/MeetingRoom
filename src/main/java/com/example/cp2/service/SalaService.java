package com.example.cp2.service;

import com.example.cp2.dto.SalaDTO;
import com.example.cp2.entities.Sala;
import com.example.cp2.exception.ResourceNotFoundException;
import com.example.cp2.repository.SalaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalaService {

    private static final Logger log = LoggerFactory.getLogger(SalaService.class);

    private final SalaRepository repository;

    public SalaService(SalaRepository repository) {
        this.repository = repository;
    }

    @CacheEvict(value = "salas", allEntries = true)
    public SalaDTO.Response criar(SalaDTO.Request dto) {
        log.info("Criando sala: nome={}", dto.getNome());
        Sala sala = Sala.builder()
                .nome(dto.getNome())
                .capacidade(dto.getCapacidade())
                .localizacao(dto.getLocalizacao())
                .build();
        return toDTO(repository.save(sala));
    }

    @Cacheable("salas")
    public List<SalaDTO.Response> listar(String localizacao, Integer capacidadeMinima) {
        log.debug("Listando salas: localizacao={}, capacidadeMinima={}", localizacao, capacidadeMinima);

        if (localizacao != null && !localizacao.isBlank()) {
            return repository.findByLocalizacaoContainingIgnoreCase(localizacao)
                    .stream().map(this::toDTO).collect(Collectors.toList());
        }
        if (capacidadeMinima != null) {
            return repository.findByCapacidadeGreaterThanEqual(capacidadeMinima)
                    .stream().map(this::toDTO).collect(Collectors.toList());
        }
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Cacheable(value = "sala", key = "#id")
    public SalaDTO.Response buscarPorId(Long id) {
        log.debug("Buscando sala: id={}", id);
        return toDTO(findOrThrow(id));
    }

    @CacheEvict(value = {"salas", "sala"}, allEntries = true)
    public SalaDTO.Response atualizar(Long id, SalaDTO.Request dto) {
        log.info("Atualizando sala: id={}", id);
        Sala sala = findOrThrow(id);
        sala.setNome(dto.getNome());
        sala.setCapacidade(dto.getCapacidade());
        sala.setLocalizacao(dto.getLocalizacao());
        return toDTO(repository.save(sala));
    }

    @CacheEvict(value = {"salas", "sala"}, allEntries = true)
    public void remover(Long id) {
        log.info("Removendo sala: id={}", id);
        repository.delete(findOrThrow(id));
    }


    private Sala findOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sala não encontrada: id=" + id));
    }

    private SalaDTO.Response toDTO(Sala sala) {
        SalaDTO.Response dto = new SalaDTO.Response();
        dto.setId(sala.getId());
        dto.setNome(sala.getNome());
        dto.setCapacidade(sala.getCapacidade());
        dto.setLocalizacao(sala.getLocalizacao());
        return dto;
    }
}