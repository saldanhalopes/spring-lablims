package br.com.lablims.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LoteStatusDTO {

    private Integer id;

    private LocalDateTime dataStatus;

    private LocalDateTime dataPrevisao;

    @Schema(type = "string", example = "18:30")
    private LocalTime dataProgramado;

    private LocalDateTime dataConferencia1;

    private LocalDateTime dataConferencia2;

    @Size(max = 255)
    private String obs;

    private Integer lote;

    private Integer planoAnalise;

    private Integer analiseStatus;

    private Integer conferente1;

    private Integer conferente2;

}
