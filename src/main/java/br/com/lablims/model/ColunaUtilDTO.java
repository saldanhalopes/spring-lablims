package br.com.lablims.model;

import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ColunaUtilDTO {

    private Integer id;

    @Size(max = 255)
    private String codigoColuna;

    @Size(max = 255)
    private String serialNumber;

    @Size(max = 255)
    private String dataAtivacao;

    @Size(max = 255)
    private String dataVerificacao;

    @Size(max = 255)
    private String dataDescarte;

    private Boolean estoque;

    @Size(max = 255)
    private String obs;

    private Integer coluna;

    private Integer setor;

    private Integer metodologiaVersao;

    private Integer analise;

    private Integer colunaVaga;

    private Integer certificado;

    private List<Integer> anexos;

}
