package br.com.lablims.model;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EquipamentoDTO {

    private Integer id;

    @Size(max = 255)
    private String descricao;

    @Size(max = 255)
    private String tag;

    @Size(max = 255)
    private String fabricante;

    @Size(max = 255)
    private String marca;

    @Size(max = 255)
    private String modelo;

    private LocalDateTime ultimaCalibracao;

    private LocalDateTime proximaCalibracao;

    private Boolean ativo;

    @Size(max = 255)
    private String obs;

    private Long imagem;

    @Size(max = 255)
    private String serialNumber;

    private Integer tipo;

    private Integer setor;

    private Integer certificado;

    private Integer manual;

    private Integer procedimento;

    private Integer escala;

}
