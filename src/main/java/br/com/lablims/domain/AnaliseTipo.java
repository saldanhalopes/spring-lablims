package br.com.lablims.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Getter
@Setter
@Audited(withModifiedFlag = true)
public class AnaliseTipo {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String analiseTipo;

    @Column
    private String siglaAnaliseTipo;

    @Column
    private String descricaoAnaliseTipo;

    @Version
    private Long version;

}
