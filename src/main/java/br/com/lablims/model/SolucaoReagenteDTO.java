package br.com.lablims.model;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SolucaoReagenteDTO {

    private Integer id;

    @Size(max = 255)
    private String lote;

    private Double qtd;

    @Size(max = 255)
    private String fabricante;

    private LocalDateTime validade;

    private Integer solucaoRegistro;

    private Integer reagente;

    private Integer unidade;

}
