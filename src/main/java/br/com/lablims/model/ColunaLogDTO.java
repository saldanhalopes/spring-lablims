package br.com.lablims.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ColunaLogDTO {

    private Integer id;

    @Size(max = 255)
    private String dataIncio;

    @Size(max = 255)
    private String dataFim;

    @Size(max = 255)
    private String sentido;

    @Size(max = 255)
    private String precoluna;

    @Size(max = 255)
    private String prefiltro;

    @Size(max = 255)
    private String injecoes;

    private Integer colunaUtil;

    private Integer usuarioInicio;

    private Integer usuarioFim;

    private Integer campanha;

    private Integer analise;

    private Integer equipamento;

    private Integer anexo;

}
