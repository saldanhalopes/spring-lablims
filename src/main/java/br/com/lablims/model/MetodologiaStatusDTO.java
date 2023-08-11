package br.com.lablims.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MetodologiaStatusDTO {

    private Integer id;

    @Size(max = 255)
    private String status;

}
