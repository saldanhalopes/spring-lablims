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
public class Reagente {

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
    private String reagente;

    @Column
    private String casNumber;

    @Column
    private Integer qtdEstoqueMin;

    @Column
    private Integer qtdEstoqueMax;

    @Column
    private Boolean compraUnica;

    @Column
    private String enderecamento;

    @Column
    private String numeroIdentificacao;

    @Column
    private String armazenamento;

    @Column
    private String grau;

    @Column
    private String pureza;

    @Column
    private String classe;

    @Column
    private String controlado;

    @Column
    private Integer saude;

    @Column
    private Integer inflamabilidade;

    @Column
    private Integer reatividade;

    @Column
    private String especifico;

    @Column
    private String obs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_id")
    private UnidadeMedida unidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fds_id")
    private Arquivos fds;

}
