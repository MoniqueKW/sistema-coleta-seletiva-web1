package br.com.brumon.siscoletaseletiva.DTO.Response;

import java.math.BigDecimal;

public record MaterialAceitoResponseDTO(
        Integer idPonto,
        Integer idMaterial,
        String nomeMaterial,
        String observacoes,
        BigDecimal limiteOpcional
) {
}
