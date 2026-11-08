package br.com.brumon.siscoletaseletiva.dto.response;

import java.time.LocalDateTime;

public record HistoricoResponseDTO(
        Integer id,
        Integer idSolicitacao,
        String situacaoAnterior,
        String situacaoNova,
        LocalDateTime data,
        String responsavel,
        String motivo,
        String infoTentativa
) {
}
