package br.com.brumon.siscoletaseletiva.DTO;

import java.time.LocalDateTime;

public record SolicitacaoResponseDTO(
        Integer id,
        Integer idMorador,
        Integer idPonto,
        String protocoloUnico,
        String copiaLogradouro,
        String copiaNumero,
        String copiaBairro,
        String copiaMunicipio,
        String copiaUf,
        String copiaCep,
        String copiaComplemento,
        String situacao,
        LocalDateTime dataCriacao,
        LocalDateTime dataConclusao,
        String observacoes,
        Integer versao
) {
}
