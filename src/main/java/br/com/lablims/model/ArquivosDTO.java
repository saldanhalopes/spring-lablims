package br.com.lablims.model;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ArquivosDTO {

    private Integer id;

    @Size(max = 255)
    private String nome;

    @Size(max = 255)
    private String tipo;

    @Size(max = 255)
    private String descricao;

    private Double tamanho;

    private Long arquivo;

    private LocalDateTime dataCriacao;

}
