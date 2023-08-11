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
public class PlanoAnalise {

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
    private String descricao;

    @Column
    private Integer leadTimeMin;

    @Column
    private Integer leadTimeMedio;

    @Column
    private Integer leadTimeMax;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metodologia_versao_id")
    private MetodologiaVesao metodologiaVersao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analise_id")
    private Analise analise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analise_tipo_id")
    private AnaliseTipo analiseTipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setor_id")
    private Setor setor;

}
