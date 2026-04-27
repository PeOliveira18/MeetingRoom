package com.example.cp2.service;

import com.example.cp2.dto.SalaDTO;
import com.example.cp2.entities.Sala;
import com.example.cp2.exception.ResourceNotFoundException;
import com.example.cp2.repository.SalaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do SalaService")
class SalaServiceTest {

    @Mock
    private SalaRepository salaRepository;

    @InjectMocks
    private SalaService salaService;

    private SalaDTO.Request salaRequest;
    private Sala sala;

    @BeforeEach
    void setUp() {
        salaRequest = new SalaDTO.Request();
        salaRequest.setNome("Sala de Reunião A");
        salaRequest.setCapacidade(10);
        salaRequest.setLocalizacao("1º Andar");

        sala = Sala.builder()
                .id(1L)
                .nome("Sala de Reunião A")
                .capacidade(10)
                .localizacao("1º Andar")
                .build();
    }

    @Test
    @DisplayName("Deve criar sala com sucesso")
    void deveCriarSalaComSucesso() {
    
        when(salaRepository.save(any(Sala.class))).thenReturn(sala);        
        SalaDTO.Response response = salaService.criar(salaRequest);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Sala de Reunião A", response.getNome());
        assertEquals(10, response.getCapacidade());
        assertEquals("1º Andar", response.getLocalizacao());
        
        verify(salaRepository, times(1)).save(any(Sala.class));
    }

    @Test
    @DisplayName("Deve filtrar salas por capacidade mínima")
    void deveFiltrarSalasPorCapacidadeMinima() {
        // Arrange
        Sala salaGrande = Sala.builder()
                .id(2L)
                .nome("Auditório")
                .capacidade(50)
                .localizacao("Térreo")
                .build();

        when(salaRepository.findByCapacidadeGreaterThanEqual(20))
                .thenReturn(Arrays.asList(salaGrande));

        
        List<SalaDTO.Response> response = salaService.listar(null, 20);
        
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Auditório", response.get(0).getNome());
        assertEquals(50, response.get(0).getCapacidade());
        
        verify(salaRepository, times(1)).findByCapacidadeGreaterThanEqual(20);
        verify(salaRepository, never()).findAll();
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar sala inexistente")
    void deveLancarExcecaoQuandoSalaNaoExiste() {
        when(salaRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            salaService.buscarPorId(1L);
        });

        assertTrue(exception.getMessage().contains("Sala não encontrada"));
        verify(salaRepository, times(1)).findById(1L);
    }
}

