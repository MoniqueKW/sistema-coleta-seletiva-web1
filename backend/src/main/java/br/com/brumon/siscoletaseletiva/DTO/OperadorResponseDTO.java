package br.com.brumon.siscoletaseletiva.DTO;

public record OperadorResponseDTO(
        Integer id,
        Integer idPonto,
        String nome,
        String email
) {
}
