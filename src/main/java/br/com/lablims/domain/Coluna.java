package br.com.lablims.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Coluna {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Integer id;

    @Column
    private String codigo;

    @Column
    private String partNumber;

    @Column
    private String obs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_coluna_id")
    private ColunaConfig tipoColuna;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fabricante_coluna_id")
    private ColunaConfig fabricanteColuna;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_coluna_id")
    private ColunaConfig marcaColuna;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fase_coluna_id")
    private ColunaConfig faseColuna;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tamanho_coluna_id")
    private ColunaConfig tamanhoColuna;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diametro_coluna_id")
    private ColunaConfig diametroColuna;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "particula_coluna_id")
    private ColunaConfig particulaColuna;

}
