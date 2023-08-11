package br.com.lablims.model;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CampanhaDTO {

    private Integer id;

    private LocalDateTime previsaoInicio;

    private LocalDateTime previsaoFim;

    private LocalDateTime dataInicio;

    private LocalDateTime dataFim;

    @Size(max = 255)
    private String status;

    @Size(max = 255)
    private String obs;

    @Size(max = 255)
    private String ordem;

    @Size(max = 255)
    private String prioridade;

    private Integer setor;

    private Integer celula;

    private List<Integer> lotes;

}
