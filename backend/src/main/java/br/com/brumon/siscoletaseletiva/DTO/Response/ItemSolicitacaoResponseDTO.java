package br.com.brumon.siscoletaseletiva.DTO.Response;

import java.math.BigDecimal;

public record ItemSolicitacaoResponseDTO(
        Integer idSolicitacao,
        Integer idMaterial,
        String copiaUnidade,
        BigDecimal quantidadeEstimada,
        BigDecimal quantidadeEfetiva
) {
}
