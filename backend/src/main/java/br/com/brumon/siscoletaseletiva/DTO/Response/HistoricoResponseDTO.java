package br.com.brumon.siscoletaseletiva.DTO.Response;

import br.com.brumon.siscoletaseletiva.Entity.Solicitacao;

import java.time.LocalDateTime;

public record HistoricoResponseDTO(
        Integer id,
        Solicitacao solicitacao,
        String situacaoAnterior,
        String situacaoNova,
        LocalDateTime data,
        String responsavel,
        String motivo,
        String infoTentativa
) {
}
