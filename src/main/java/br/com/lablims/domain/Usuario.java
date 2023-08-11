package br.com.lablims.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Usuario {

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
    private String cep;

    @Column
    private Boolean changePass;

    @Column
    private String cidade;

    @Column
    private String cpf;

    @Column
    private String detalhes;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String endereco;

    @Column
    private String estado;

    @Column
    private Integer failedAccessCount;

    @Column
    private OffsetDateTime lastChangePass;

    @Column
    private OffsetDateTime lastLogin;

    @Column
    private OffsetDateTime lastLogout;

    @Column
    private String nome;

    @Column
    private String pais;

    @Column(nullable = false)
    private String password;

    @Column
    private String sobrenome;

    @Column
    private String telefone;

    @Column(nullable = false, unique = true)
    private String username;

    @ManyToMany
    @JoinTable(
            name = "usuario_grupo",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "grupo_id")
    )
    private Set<Grupo> grupos;

}
