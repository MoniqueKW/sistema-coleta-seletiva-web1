package br.com.brumon.siscoletaseletiva.DTO.Response;

public record OperadorResponseDTO(
        Integer id,
        Integer idPonto,
        String nome,
        String email
) {
}
