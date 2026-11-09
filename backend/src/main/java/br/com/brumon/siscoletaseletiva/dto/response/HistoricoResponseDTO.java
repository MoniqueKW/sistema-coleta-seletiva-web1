package br.com.brumon.siscoletaseletiva.dto.response;

import java.time.OffsetDateTime;

public record HistoricoResponseDTO(
        Integer id,
        Integer idSolicitacao,
        String situacaoAnterior,
        String situacaoNova,
        OffsetDateTime data,
        String responsavel,
        Integer idOperador,
        String motivo,
        String infoTentativa
) {
}
