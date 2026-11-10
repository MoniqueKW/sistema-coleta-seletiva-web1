package br.com.brumon.siscoletaseletiva.dto.response;

import br.com.brumon.siscoletaseletiva.model.enums.SituacaoSolicitacao;

public record ResumoSituacaoDTO(
        SituacaoSolicitacao situacao,
        Long total
) {
}