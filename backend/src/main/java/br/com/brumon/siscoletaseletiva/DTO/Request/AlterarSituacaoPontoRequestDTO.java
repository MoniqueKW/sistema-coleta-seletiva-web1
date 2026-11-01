package br.com.brumon.siscoletaseletiva.DTO.Request;

import br.com.brumon.siscoletaseletiva.Enum.SituacaoPonto;
import jakarta.validation.constraints.NotNull;

public record AlterarSituacaoPontoRequestDTO(
        @NotNull
        SituacaoPonto situacao
) {
}
