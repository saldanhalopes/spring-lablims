package br.com.lablims.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;


@Entity
@Getter
@Setter
@Audited(withModifiedFlag = true)
public class SolucaoParemetro {

    @Version
    private Short version;

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String paremetro;

    @Column
    private String valor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solucao_registro_id")
    private SolucaoRegistro solucaoRegistro;

}
