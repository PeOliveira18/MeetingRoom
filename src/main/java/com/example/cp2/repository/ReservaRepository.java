package com.example.cp2.repository;



import com.example.cp2.entities.Reserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    Page<Reserva> findByStatus(Reserva.StatusReserva status, Pageable pageable);

    List<Reserva> findByResponsavelContainingIgnoreCase(String responsavel);

    List<Reserva> findBySalaId(Long salaId);

    @Query("""
        SELECT COUNT(r) > 0 FROM Reserva r
        WHERE r.sala.id = :salaId
          AND r.status = 'ATIVA'
          AND r.id <> :excludeId
          AND r.dataHoraInicio < :fim
          AND r.dataHoraFim > :inicio
    """)
    boolean existeConflito(
            @Param("salaId") Long salaId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("excludeId") Long excludeId
    );
}