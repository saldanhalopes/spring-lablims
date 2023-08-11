package br.com.lablims.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class ColunaUtil {

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
    private String codigoColuna;

    @Column
    private String serialNumber;

    @Column
    private String dataAtivacao;

    @Column
    private String dataVerificacao;

    @Column
    private String dataDescarte;

    @Column
    private Boolean estoque;

    @Column
    private String obs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coluna_id")
    private Coluna coluna;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setor_id")
    private Setor setor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metodologia_versao_id")
    private MetodologiaVesao metodologiaVersao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analise_id")
    private Analise analise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coluna_vaga_id")
    private ColunaVaga colunaVaga;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certificado_id")
    private Arquivos certificado;

    @ManyToMany
    @JoinTable(
            name = "arquivos_coluna_util",
            joinColumns = @JoinColumn(name = "coluna_util_id"),
            inverseJoinColumns = @JoinColumn(name = "arquivos_id")
    )
    private Set<Arquivos> anexos;

}
