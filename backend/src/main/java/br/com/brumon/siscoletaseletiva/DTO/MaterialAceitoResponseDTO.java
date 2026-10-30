package br.com.brumon.siscoletaseletiva.DTO;

import java.math.BigDecimal;

public record MaterialAceitoResponseDTO(
        Integer idPonto,
        Integer idMaterial,
        String observacoes,
        BigDecimal limiteOpcional
) {
}
