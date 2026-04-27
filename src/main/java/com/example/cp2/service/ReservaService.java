package com.example.cp2.service;


import com.example.cp2.dto.ReservaDTO;
import com.example.cp2.entities.Reserva;
import com.example.cp2.entities.Sala;
import com.example.cp2.exception.BusinessException;
import com.example.cp2.exception.ResourceNotFoundException;
import com.example.cp2.repository.ReservaRepository;
import com.example.cp2.repository.SalaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    private static final Logger log = LoggerFactory.getLogger(ReservaService.class);

    private final ReservaRepository reservaRepo;
    private final SalaRepository salaRepo;

    public ReservaService(ReservaRepository reservaRepo, SalaRepository salaRepo) {
        this.reservaRepo = reservaRepo;
        this.salaRepo = salaRepo;
    }

    public ReservaDTO.Response criar(ReservaDTO.Request dto) {
        log.info("Criando reserva: sala={}, responsavel={}", dto.getSalaId(), dto.getResponsavel());

        if (!dto.getDataHoraFim().isAfter(dto.getDataHoraInicio())) {
            throw new BusinessException("Data/hora de fim deve ser após o início");
        }

        Sala sala = salaRepo.findById(dto.getSalaId())
                .orElseThrow(() -> new ResourceNotFoundException("Sala não encontrada: id=" + dto.getSalaId()));

        boolean conflito = reservaRepo.existeConflito(
                dto.getSalaId(),
                dto.getDataHoraInicio(),
                dto.getDataHoraFim(),
                -1L // -1 para não excluir nenhuma reserva na criação
        );

        if (conflito) {
            log.warn("Conflito de reserva detectado: sala={}, inicio={}", dto.getSalaId(), dto.getDataHoraInicio());
            throw new BusinessException("Já existe uma reserva ativa para essa sala nesse horário");
        }

        Reserva reserva = Reserva.builder()
                .sala(sala)
                .dataHoraInicio(dto.getDataHoraInicio())
                .dataHoraFim(dto.getDataHoraFim())
                .responsavel(dto.getResponsavel())
                .build();

        return toDTO(reservaRepo.save(reserva));
    }

    public Page<ReservaDTO.Response> listar(int page, int size, String responsavel) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dataHoraInicio").ascending());

        if (responsavel != null && !responsavel.isBlank()) {
            List<ReservaDTO.Response> lista = reservaRepo
                    .findByResponsavelContainingIgnoreCase(responsavel)
                    .stream().map(this::toDTO).collect(Collectors.toList());
            return new PageImpl<>(lista, pageable, lista.size());
        }

        return reservaRepo.findAll(pageable).map(this::toDTO);
    }

    public void cancelar(Long id) {
        log.info("Cancelando reserva: id={}", id);
        Reserva reserva = reservaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada: id=" + id));

        if (reserva.getStatus() == Reserva.StatusReserva.CANCELADA) {
            throw new BusinessException("Reserva já está cancelada");
        }

        reserva.setStatus(Reserva.StatusReserva.CANCELADA);
        reservaRepo.save(reserva);
    }

    private ReservaDTO.Response toDTO(Reserva r) {
        ReservaDTO.Response dto = new ReservaDTO.Response();
        dto.setId(r.getId());
        dto.setSalaId(r.getSala().getId());
        dto.setNomeSala(r.getSala().getNome());
        dto.setDataHoraInicio(r.getDataHoraInicio());
        dto.setDataHoraFim(r.getDataHoraFim());
        dto.setResponsavel(r.getResponsavel());
        dto.setStatus(r.getStatus().name());
        return dto;
    }
}