package br.com.brumon.siscoletaseletiva.dto.response;

import java.math.BigDecimal;

public record ItemSolicitacaoResponseDTO(
        Integer idSolicitacao,
        Integer idMaterial,
        String nomeMaterial,
        String copiaUnidade,
        BigDecimal quantidadeEstimada,
        BigDecimal quantidadeEfetiva
) {
}
