package br.com.lablims.model;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MetodologiaVesaoDTO {

    private Integer id;

    private LocalDateTime dataRevisao;

    private LocalDateTime dataProximaRevisao;

    @Size(max = 255)
    private String obs;

    private Integer metodologia;

    private Integer anexo;

    private List<Integer> material;

    private Integer status;

}
