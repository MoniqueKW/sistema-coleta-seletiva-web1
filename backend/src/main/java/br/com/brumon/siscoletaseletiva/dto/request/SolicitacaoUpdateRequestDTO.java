package br.com.brumon.siscoletaseletiva.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

//feito pra atualizar a Solicitacao, nao possui as datas e nem como
//alterar o id do morador, pois se fosse para mudar o id do morador
// e tambem o do ponto, é a mesma coisa que criar outra solicitação.
// esse dto só pode ser usado se a solicitacao estiver marcada como SOLICITADA
// ainda, depois, nao pode alterar mais nada a nao ser a situacao
public record SolicitacaoUpdateRequestDTO(
        Integer idPonto,
        Integer idEndereco,
        @Size(max = 255)
        String observacoes,
        Boolean recolhimentoDomiciliar,
        //a versao precisa ser passada para conferir se bate com a versao atual e impedir
        //problemas de concorrencia com a versao
        @NotNull
        Integer versao,
        List<@Valid ItemSolicitacaoRequestDTO> itens
) {
}
