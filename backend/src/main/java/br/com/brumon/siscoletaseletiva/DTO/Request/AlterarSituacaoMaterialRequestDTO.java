package br.com.brumon.siscoletaseletiva.DTO.Request;

import br.com.brumon.siscoletaseletiva.Enum.SituacaoMaterial;
import jakarta.validation.constraints.NotNull;

public record AlterarSituacaoMaterialRequestDTO(
        @NotNull
        SituacaoMaterial situacao
) {
}
