package br.com.brumon.siscoletaseletiva.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ItemConclusaoRequestDTO(
        @NotNull
        Integer idMaterial,
        @NotNull
        @PositiveOrZero
        BigDecimal quantidadeEfetiva
) {
}
