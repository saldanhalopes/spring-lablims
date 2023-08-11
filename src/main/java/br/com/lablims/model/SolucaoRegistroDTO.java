package br.com.lablims.model;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SolucaoRegistroDTO {

    private Integer id;

    @Size(max = 255)
    private String descricao;

    private LocalDateTime lote;

    @Size(max = 255)
    private String tipo;

    @Size(max = 255)
    private String referencia;

    @Size(max = 255)
    private String armazenamento;

    private LocalDateTime dataPreparo;

    private LocalDateTime dataValidade;

    private LocalDateTime dataConferencia;

    private Double qtd;

    private Boolean ativo;

    @Size(max = 255)
    private String obs;

    private Integer solucaoTipo;

    private Integer criador;

    private Integer conferente;

    private Integer unidade;

}
