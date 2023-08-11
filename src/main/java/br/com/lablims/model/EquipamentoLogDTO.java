package br.com.lablims.model;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EquipamentoLogDTO {

    private Integer id;

    @Size(max = 255)
    private String descricao;

    private LocalDateTime dataInicio;

    private LocalDateTime dataFim;

    @Size(max = 255)
    private String obs;

    private Integer atividade;

    private Integer equipamento;

    private Integer usuarioInicio;

    private Integer usuarioFim;

    private Integer anexo;

}
