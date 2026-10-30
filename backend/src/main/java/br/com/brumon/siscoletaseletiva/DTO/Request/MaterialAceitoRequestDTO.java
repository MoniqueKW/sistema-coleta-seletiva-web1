package br.com.brumon.siscoletaseletiva.DTO.Request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

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
