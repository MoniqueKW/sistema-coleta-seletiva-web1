package br.com.brumon.siscoletaseletiva.DTO;

public record PontoColetaResponseDTO(
        Integer idPonto,
        String nome,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String municipio,
        String uf,
        String cep,
        String contato,
        String horarios,
        String situacao,
        Boolean recolhimentoDomiciliar
) {
}
