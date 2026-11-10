package br.com.brumon.siscoletaseletiva.dto.request;

import jakarta.validation.constraints.*;

public record OperadorRequestDTO(
        @NotBlank
        @Size(max = 150)
        @Pattern(
                // regex que aceita letras maiúsculas/minúsculas, acentos, espaços, hífens e apóstrofos
                regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s'-]+$",
                message = "O nome não deve conter números ou caracteres especiais inválidos"
        )
        String nome,
        @NotBlank
        @Email(
                regexp = "^(?!.*\\.\\.)[A-Za-z0-9](?:[A-Za-z0-9._%+-]*[A-Za-z0-9])?@(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z]{2,}$",
                message = "Formato de e-mail inválido."
        )
        @Size(max = 255)
        String email
) {
}
