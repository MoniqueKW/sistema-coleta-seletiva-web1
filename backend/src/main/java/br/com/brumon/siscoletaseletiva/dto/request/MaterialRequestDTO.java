package br.com.brumon.siscoletaseletiva.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MaterialRequestDTO(
        @NotBlank
        @Size(max = 100)
        String nome,
        @Size(max = 255)
        String descricao,
        @NotBlank
        @Size(max = 20)
        String unidade,
        @Size(max = 255)
        String orientacao
) {

}
