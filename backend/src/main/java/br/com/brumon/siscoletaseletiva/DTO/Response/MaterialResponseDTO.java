package br.com.brumon.siscoletaseletiva.DTO.Response;

import br.com.brumon.siscoletaseletiva.Enum.SituacaoMaterial;

public record MaterialResponseDTO(
        Integer id,
        String nome,
        String descricao,
        String unidade,
        String orientacao,
        SituacaoMaterial situacao
) {
}
