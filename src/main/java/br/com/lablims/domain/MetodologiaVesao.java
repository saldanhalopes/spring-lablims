package br.com.lablims.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;


@Entity
@Getter
@Setter
@Audited(withModifiedFlag = true)
public class MetodologiaVesao {

    @Version
    private Short version;

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
