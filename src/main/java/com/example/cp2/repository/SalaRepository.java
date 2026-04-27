package com.example.cp2.repository;


import com.example.cp2.entities.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {
    List<Sala> findByLocalizacaoContainingIgnoreCase(String localizacao);

    List<Sala> findByCapacidadeGreaterThanEqual(Integer capacidadeMinima);
}