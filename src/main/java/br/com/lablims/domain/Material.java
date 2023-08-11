package br.com.lablims.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Material {

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
    private Boolean controleEspecial;

    @Column
    private String fiscalizado;

    @Column
    private Integer item;

    @Column
    private String material;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_material_id")
    private MaterialTipo tipoMaterial;

    @ManyToMany(mappedBy = "material")
    private Set<MetodologiaVesao> metodologiaVersao;

}
