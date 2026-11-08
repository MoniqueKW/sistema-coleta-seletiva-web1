package br.com.brumon.siscoletaseletiva.dto.request;

import br.com.brumon.siscoletaseletiva.model.enums.SituacaoPonto;
import jakarta.validation.constraints.NotNull;

public record AlterarSituacaoPontoRequestDTO(
        @NotNull
        SituacaoPonto situacao
) {
}
