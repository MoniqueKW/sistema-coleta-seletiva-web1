package br.com.brumon.siscoletaseletiva.DTO;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

//Feito para Controller de Data de Agendamento de Solicitação
public record AgendamentoRequestDTO(
        @NotNull
        @Future
        LocalDateTime dataAgendamento
) {
}
