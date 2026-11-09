package br.com.brumon.siscoletaseletiva.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record SolicitacaoRequestDTO(
        @NotNull
        Integer idMorador,
        @NotNull
        Integer idPonto,
        @NotNull
        Integer idEndereco,
        @Size(max = 255)
        String observacoes,
        @NotNull
        Boolean recolhimentoDomiciliar,
        @NotEmpty
        List<@Valid ItemSolicitacaoRequestDTO> itens
) {
}
