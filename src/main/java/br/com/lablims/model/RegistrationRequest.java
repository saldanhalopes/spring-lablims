package br.com.lablims.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;


@Getter
@Setter
public class RegistrationRequest {

    @Size(max = 255)
    private String cep;

    private Boolean changePass;

    @Size(max = 255)
    private String cidade;

    @Size(max = 255)
    private String cpf;

    @Size(max = 255)
    private String detalhes;

    @NotNull
    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String endereco;

    @Size(max = 255)
    private String estado;

    private Integer failedAccessCount;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime lastChangePass;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime lastLogin;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime lastLogout;

    @Size(max = 255)
    private String nome;

    @Size(max = 255)
    private String pais;

    @NotNull
    @Size(max = 255)
    private String password;

    @Size(max = 255)
    private String sobrenome;

    @Size(max = 255)
    private String telefone;

    @NotNull
    @Size(max = 255)
    private String username;

}
