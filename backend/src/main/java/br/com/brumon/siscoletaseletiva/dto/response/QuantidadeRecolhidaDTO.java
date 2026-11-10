package br.com.brumon.siscoletaseletiva.dto.response;

import java.math.BigDecimal;

public record QuantidadeRecolhidaDTO(
        Integer idMaterial,
        String nomeMaterial,
        String unidade,
        BigDecimal totalRecolhido
) {
}