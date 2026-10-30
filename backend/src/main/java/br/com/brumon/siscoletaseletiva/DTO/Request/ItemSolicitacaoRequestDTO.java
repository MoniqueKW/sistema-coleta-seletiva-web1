package br.com.brumon.siscoletaseletiva.DTO.Request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ItemSolicitacaoRequestDTO(
        @NotNull
        Integer idMaterial,
        @NotNull
        @Positive
        BigDecimal qtdEstimada
) {
}