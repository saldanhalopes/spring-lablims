package br.com.lablims.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ColunaConfigDTO {

    private Integer id;

    @Size(max = 255)
    private String tipo;

    @Size(max = 255)
    private String configuracao;

    @Size(max = 255)
    private String descricao;

    private Short version;

}
