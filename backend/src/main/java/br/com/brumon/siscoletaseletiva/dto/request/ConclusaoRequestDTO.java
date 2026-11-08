package br.com.brumon.siscoletaseletiva.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record ConclusaoRequestDTO(
        @NotEmpty
        List<@Valid ItemConclusaoRequestDTO> itens
) {
}
