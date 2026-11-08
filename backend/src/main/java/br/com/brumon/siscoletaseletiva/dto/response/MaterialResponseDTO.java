package br.com.brumon.siscoletaseletiva.dto.response;

import br.com.brumon.siscoletaseletiva.model.enums.SituacaoMaterial;

public record MaterialResponseDTO(
        Integer id,
        String nome,
        String descricao,
        String unidade,
        String orientacao,
        SituacaoMaterial situacao
) {
}
