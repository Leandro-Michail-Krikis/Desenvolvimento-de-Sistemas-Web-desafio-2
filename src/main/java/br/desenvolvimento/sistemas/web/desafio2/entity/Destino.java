package br.desenvolvimento.sistemas.web.desafio2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "destino")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Destino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private String pais;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "nota_media")
    private Double notaMedia = 0.0;

    @Column(name = "total_notas")
    private Integer totalNotas = 0;

    @OneToMany(
            mappedBy = "destino",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Nota> notas;
}

