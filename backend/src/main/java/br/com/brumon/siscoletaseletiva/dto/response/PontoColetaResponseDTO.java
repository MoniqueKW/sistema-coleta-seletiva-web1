package br.com.brumon.siscoletaseletiva.dto.response;

import br.com.brumon.siscoletaseletiva.model.enums.SituacaoPonto;

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
        SituacaoPonto situacao,
        Boolean recolhimentoDomiciliar
) {
}
