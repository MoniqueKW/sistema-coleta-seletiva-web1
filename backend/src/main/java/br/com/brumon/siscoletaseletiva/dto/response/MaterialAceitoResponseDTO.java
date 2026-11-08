package br.com.brumon.siscoletaseletiva.dto.response;

import java.math.BigDecimal;

public record MaterialAceitoResponseDTO(
        Integer idPonto,
        Integer idMaterial,
        String nomeMaterial,
        String observacoes,
        BigDecimal limiteOpcional
) {
}
