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
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Lote {

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
    private String lote;

    @Column
    private Double qtdEstoque;

    @Column
    private LocalDateTime dataStatus;

    @Column
    private LocalDateTime dataEntrada;

    @Column
    private LocalDateTime dataInicioAnalise;

    @Column
    private LocalDateTime dataLiberacao;

    @Column
    private LocalDateTime dataEnvioGarantia;

    @Column
    private LocalDateTime dataNecessidade;

    @Column
    private LocalDateTime dataValidade;

    @Column
    private String dataImpressao;

    @Column
    private String numeroDocumento;

    @Column
    private String complemento;

    @Column
    private String obs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id")
    private Material material;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amostra_tipo_id")
    private AmostraTipo amostraTipo;

}
