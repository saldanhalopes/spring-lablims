package br.com.lablims.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class LoteStatus {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Integer id;

    @Column
    private LocalDateTime dataStatus;

    @Column
    private LocalDateTime dataPrevisao;

    @Column
    private LocalTime dataProgramado;

    @Column
    private LocalDateTime dataConferencia1;

    @Column
    private LocalDateTime dataConferencia2;

    @Column
    private String obs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id")
    private Lote lote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plano_analise_id")
    private PlanoAnalise planoAnalise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analise_status_id")
    private AnaliseStatus analiseStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conferente1_id")
    private Usuario conferente1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conferente2_id")
    private Usuario conferente2;

}
