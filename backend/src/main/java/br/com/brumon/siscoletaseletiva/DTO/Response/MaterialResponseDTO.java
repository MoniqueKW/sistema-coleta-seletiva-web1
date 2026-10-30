package br.com.brumon.siscoletaseletiva.DTO.Response;

public record MaterialResponseDTO(
        Integer id,
        String nome,
        String descricao,
        String unidade,
        String orientacao,
        String situacao
) {
}
