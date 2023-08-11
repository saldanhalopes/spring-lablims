package br.com.lablims.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ColunaStorageDTO {

    private Integer id;

    @Size(max = 255)
    private String numero;

    @Size(max = 255)
    private String obs;

    private Integer setor;

    private Integer tipo;

}
