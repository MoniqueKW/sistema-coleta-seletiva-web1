package br.com.brumon.siscoletaseletiva.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PontoColetaRequestDTO(
        @NotBlank
        @Size(max = 150)
        String nome,
        @NotBlank
        @Size(max = 150)
        String logradouro,
        @NotBlank
        @Size(max = 20)
        String numero,
        @Size(max = 255)
        String complemento,
        @NotBlank
        @Size(max = 100)
        String bairro,
        @NotBlank
        @Size(max = 100)
        String municipio,
        @NotBlank
        @Size(max = 2)
        @Pattern(regexp = "^[A-Z]{2}$", message = "UF deve conter exatamente 2 letras maiúsculas")
        String uf,
        @NotBlank
        @Size(max = 10)
        String cep,
        @Size(max = 255)
        String contato,
        @Size(max = 255)
        String horarios,
        @NotNull
        Boolean recolhimentoDomiciliar
) {
}
