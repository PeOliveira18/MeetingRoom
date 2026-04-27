package com.example.cp2.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "salas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer capacidade;

    @Column(nullable = false)
    private String localizacao;
}
