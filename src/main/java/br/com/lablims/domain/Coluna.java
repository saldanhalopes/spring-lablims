package br.com.lablims.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;


@Entity
@Getter
@Setter
@Audited(withModifiedFlag = true)
public class Coluna {

    @Version
    private Short version;

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)

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
