package br.com.brumon.siscoletaseletiva.DTO.Response;

import br.com.brumon.siscoletaseletiva.Enum.SituacaoSolicitacao;

import java.time.LocalDateTime;
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
        LocalDateTime dataCriacao,
        LocalDateTime dataAgendamento,
        LocalDateTime dataConclusao,
        String observacoes,
        boolean receolhimentoDomiciliar,
        Integer versao,
        List<ItemSolicitacaoResponseDTO> itens
) {
}
