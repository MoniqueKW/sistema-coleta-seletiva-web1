package br.com.brumon.siscoletaseletiva.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
//Não fazemos idPonto aqui, pq ele já vai estar passando pela URL
//tipo assim: POST pontos/{idPonto}/materiais/
//ou seja, o idPonto já fica explícito ali. Mas no response, ele está lá.
public record MaterialAceitoRequestDTO(
        @NotNull
        Integer idMaterial,
        @Size(max = 255)
        String observacoes,
        @Positive
        @Size(max = 255)
        BigDecimal limiteOpcional
) {
}
