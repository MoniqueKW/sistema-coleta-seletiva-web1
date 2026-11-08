package br.com.brumon.siscoletaseletiva.dto.request;

import jakarta.validation.constraints.*;

public record OperadorRequestDTO(
        @NotNull
        Integer idPonto,
        @NotBlank
        @Size(max = 150)
        String nome,
        @NotBlank
        @Email
        @Size(max = 255)
        String email
) {
}
