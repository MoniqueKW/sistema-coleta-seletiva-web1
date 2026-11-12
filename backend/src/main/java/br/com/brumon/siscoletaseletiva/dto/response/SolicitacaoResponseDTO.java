package br.com.brumon.siscoletaseletiva.dto.response;

import br.com.brumon.siscoletaseletiva.model.enums.SituacaoSolicitacao;

import java.time.OffsetDateTime;
import java.util.List;

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
        SituacaoSolicitacao situacao,
        OffsetDateTime dataCriacao,
        OffsetDateTime dataAgendamento,
        OffsetDateTime dataConclusao,
        String observacoes,
        Integer versao,
        List<ItemSolicitacaoResponseDTO> itens
) {
}
