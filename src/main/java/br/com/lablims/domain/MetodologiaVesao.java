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
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class MetodologiaVesao {

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
    private LocalDateTime dataRevisao;

    @Column
    private LocalDateTime dataProximaRevisao;

    @Column
    private String obs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metodologia_id")
    private Metodologia metodologia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anexo_id")
    private Arquivos anexo;

    @ManyToMany
    @JoinTable(
            name = "metodologia_vesao_material",
            joinColumns = @JoinColumn(name = "metodologia_vesao_id"),
            inverseJoinColumns = @JoinColumn(name = "material_id")
    )
    private Set<Material> material;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private MetodologiaStatus status;

}
