package br.com.brumon.siscoletaseletiva.controller;

import br.com.brumon.siscoletaseletiva.dto.response.ApiErroResponse;
import br.com.brumon.siscoletaseletiva.dto.response.QuantidadeRecolhidaDTO;
import br.com.brumon.siscoletaseletiva.dto.response.ResumoSituacaoDTO;
import br.com.brumon.siscoletaseletiva.service.ResumoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@Tag(name = "Resumo gerencial", description = "Indicadores de solicitações e quantidades recolhidas")
@RequestMapping("/resumo")
public class ResumoController {
    private final ResumoService resumoServ;

    public ResumoController(ResumoService resumoServ) {
        this.resumoServ = resumoServ;
    }

    @Operation(summary = "Número de solicitações por situação",
            description = "Retorna todas as situações, com total zero quando não há solicitações. " +
                    "O período filtra pela data de criação e só é aplicado quando as duas datas são enviadas. " +
                    "Datas no formato ISO com fuso (ex: 2026-11-10T08:00:00-03:00).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Totais por situação"),
            @ApiResponse(responseCode = "400", description = "Data inválida ou data inicial posterior à final",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @GetMapping("/solicitacoes-por-situacao")
    public ResponseEntity<List<ResumoSituacaoDTO>> resumoPorSituacao(
            @Parameter(description = "Data de criação inicial (opcional)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dataInicio,
            @Parameter(description = "Data de criação final (opcional)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dataFim) {
        return ResponseEntity.ok(resumoServ.resumoPorSituacao(dataInicio, dataFim));
    }

    @Operation(summary = "Quantidades recolhidas por material",
            description = "Soma a quantidade efetiva só de atendimentos CONCLUIDA, nunca a estimada. " +
                    "Cada linha é um material em uma unidade, então kg e unidades não são somados juntos. " +
                    "O período filtra pela data de conclusão e só é aplicado quando as duas datas são enviadas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Totais recolhidos por material e unidade"),
            @ApiResponse(responseCode = "400", description = "Data inválida ou data inicial posterior à final",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @GetMapping("/quantidades-recolhidas")
    public ResponseEntity<List<QuantidadeRecolhidaDTO>> quantidadesRecolhidas(
            @Parameter(description = "Data de conclusão inicial (opcional)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dataInicio,
            @Parameter(description = "Data de conclusão final (opcional)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dataFim) {
        return ResponseEntity.ok(resumoServ.quantidadesRecolhidas(dataInicio, dataFim));
    }
}