package br.com.lablims.model;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LoteDTO {

    private Integer id;

    @Size(max = 255)
    private String lote;

    private Double qtdEstoque;

    private LocalDateTime dataStatus;

    private LocalDateTime dataEntrada;

    private LocalDateTime dataInicioAnalise;

    private LocalDateTime dataLiberacao;

    private LocalDateTime dataEnvioGarantia;

    private LocalDateTime dataNecessidade;

    private LocalDateTime dataValidade;

    @Size(max = 255)
    private String dataImpressao;

    @Size(max = 255)
    private String numeroDocumento;

    @Size(max = 255)
    private String complemento;

    @Size(max = 255)
    private String obs;

    private Integer material;

    private Integer amostraTipo;

}
