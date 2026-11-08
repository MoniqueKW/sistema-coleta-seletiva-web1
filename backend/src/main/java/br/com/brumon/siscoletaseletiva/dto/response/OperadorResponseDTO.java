package br.com.brumon.siscoletaseletiva.dto.response;

public record OperadorResponseDTO(
        Integer id,
        Integer idPonto,
        String nome,
        String email
) {
}
