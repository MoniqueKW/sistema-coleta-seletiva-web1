package br.com.brumon.siscoletaseletiva.DTO;

public record EnderecoResponseDTO(
        Integer id,
        Integer idMorador,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String municipio,
        String uf,
        String cep
) {
}