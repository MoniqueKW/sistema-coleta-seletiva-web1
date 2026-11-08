package br.com.brumon.siscoletaseletiva.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

public record SolicitacaoRequestDTO(
        @NotNull
        Integer idMorador,
        @NotNull
        Integer idPonto,
        @NotBlank
        @Size(max = 150)
        String copiaLogradouro,
        @NotBlank
        @Size(max = 20)
        String copiaNumero,
        @Size(max = 255)
        String copiaComplemento,
        @NotBlank
        @Size(max = 100)
        String copiaBairro,
        @NotBlank
        @Size(max = 100)
        String copiaMunicipio,
        @NotBlank
        @Size(max = 2)
        @Pattern(regexp = "^[A-Z]{2}$", message = "UF deve conter exatamente 2 letras maiúsculas")
        String copiaUf,
        @NotBlank
        @Size(max = 10)
        String copiaCep,
        @Future
        LocalDateTime dataAgendamento,
        @Size(max = 255)
        String observacoes,
        @NotNull
        Boolean recolhimentoDomiciliar,
        @NotEmpty
        List<@Valid ItemSolicitacaoRequestDTO> itens
) {
}
