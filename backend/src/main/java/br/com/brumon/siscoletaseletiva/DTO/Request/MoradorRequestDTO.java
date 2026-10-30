package br.com.brumon.siscoletaseletiva.DTO.Request;

import jakarta.validation.constraints.*;

public record MoradorRequestDTO(
        @NotBlank
        @Size(max = 150)
        String nome,
        @NotBlank
        @Email
        @Size(max = 255)
        String email,
        @Size(max = 20)
        String telefone
) {
}