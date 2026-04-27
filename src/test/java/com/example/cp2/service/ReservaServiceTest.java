package com.example.cp2.service;

import com.example.cp2.dto.ReservaDTO;
import com.example.cp2.entities.Reserva;
import com.example.cp2.entities.Sala;
import com.example.cp2.exception.BusinessException;
import com.example.cp2.exception.ResourceNotFoundException;
import com.example.cp2.repository.ReservaRepository;
import com.example.cp2.repository.SalaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do ReservaService")
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private SalaRepository salaRepository;

    @InjectMocks
    private ReservaService reservaService;

    private Sala sala;
    private ReservaDTO.Request reservaRequest;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;

    @BeforeEach
    void setUp() {
        sala = Sala.builder()
                .id(1L)
                .nome("Sala de Reunião A")
                .capacidade(10)
                .localizacao("1º Andar")
                .build();

        dataInicio = LocalDateTime.now().plusDays(1);
        dataFim = dataInicio.plusHours(2);

        reservaRequest = new ReservaDTO.Request();
        reservaRequest.setSalaId(1L);
        reservaRequest.setDataHoraInicio(dataInicio);
        reservaRequest.setDataHoraFim(dataFim);
        reservaRequest.setResponsavel("João Silva");
    }

    @Test
    @DisplayName("Deve criar reserva com sucesso quando não há conflito")
    void deveCriarReservaComSucesso() {
        when(salaRepository.findById(1L)).thenReturn(Optional.of(sala));
        when(reservaRepository.existeConflito(anyLong(), any(), any(), anyLong())).thenReturn(false);
        
        Reserva reservaSalva = Reserva.builder()
                .id(1L)
                .sala(sala)
                .dataHoraInicio(dataInicio)
                .dataHoraFim(dataFim)
                .responsavel("João Silva")
                .status(Reserva.StatusReserva.ATIVA)
                .build();
        
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaSalva);
        ReservaDTO.Response response = reservaService.criar(reservaRequest);
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Sala de Reunião A", response.getNomeSala());
        assertEquals("João Silva", response.getResponsavel());
        assertEquals("ATIVA", response.getStatus());
        
        verify(salaRepository, times(1)).findById(1L);
        verify(reservaRepository, times(1)).existeConflito(anyLong(), any(), any(), anyLong());
        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando há conflito de horário")
    void deveLancarExcecaoQuandoHaConflitoDeHorario() {
        when(salaRepository.findById(1L)).thenReturn(Optional.of(sala));
        when(reservaRepository.existeConflito(anyLong(), any(), any(), anyLong())).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            reservaService.criar(reservaRequest);
        });

        assertEquals("Já existe uma reserva ativa para essa sala nesse horário", exception.getMessage());
        
        verify(salaRepository, times(1)).findById(1L);
        verify(reservaRepository, times(1)).existeConflito(anyLong(), any(), any(), anyLong());
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Deve cancelar reserva com sucesso")
    void deveCancelarReservaComSucesso() {
        Reserva reserva = Reserva.builder()
                .id(1L)
                .sala(sala)
                .dataHoraInicio(dataInicio)
                .dataHoraFim(dataFim)
                .responsavel("João Silva")
                .status(Reserva.StatusReserva.ATIVA)
                .build();

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);
        reservaService.cancelar(1L);

        assertEquals(Reserva.StatusReserva.CANCELADA, reserva.getStatus());
        verify(reservaRepository, times(1)).findById(1L);
        verify(reservaRepository, times(1)).save(reserva);
    }
}


