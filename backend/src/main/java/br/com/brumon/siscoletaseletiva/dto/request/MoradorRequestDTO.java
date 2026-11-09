package br.com.brumon.siscoletaseletiva.dto.request;

import jakarta.validation.constraints.*;

public record MoradorRequestDTO(
        @NotBlank
        @Size(max = 150)
        @Pattern(
                // regex que aceita letras maiúsculas/minúsculas, acentos, espaços, hífens e apóstrofos
                regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s'-]+$",
                message = "O nome não deve conter números ou caracteres especiais inválidos"
        )
        String nome,
        @NotBlank
        @Email
        @Size(max = 255)
        String email,
        @Size(max = 20)
        @Pattern(
                regexp = "^$|^(?:\\+?55\\s?)?(?:\\(?([1-9]{2})\\)?)\\s?(?:9\\s?)?[0-9]{4}-?[0-9]{4}$",
                message = "Formato de telefone inválido. Use o padrão com DDD."
        )
        String telefone
) {
}