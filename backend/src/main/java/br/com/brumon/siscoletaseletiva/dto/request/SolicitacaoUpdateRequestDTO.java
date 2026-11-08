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
        @Size(max = 150)
        String copiaLogradouro,
        @Size(max = 20)
        String copiaNumero,
        @Size(max = 255)
        String copiaComplemento,
        @Size(max = 100)
        String copiaBairro,
        @Size(max = 100)
        String copiaMunicipio,
        @Size(max = 2)
        @Pattern(regexp = "^[A-Z]{2}$", message = "UF deve conter exatamente 2 letras maiúsculas")
        String copiaUf,
        @Size(max = 10)
        String copiaCep,
        @Size(max = 255) String observacoes,
        Boolean recolhimentoDomiciliar,
        List<@Valid ItemSolicitacaoRequestDTO> itens
) {
}
