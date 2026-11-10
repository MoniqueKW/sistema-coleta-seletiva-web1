package br.com.brumon.siscoletaseletiva.controller;

import br.com.brumon.siscoletaseletiva.dto.response.ApiErroResponse;
import br.com.brumon.siscoletaseletiva.dto.response.HistoricoResponseDTO;
import br.com.brumon.siscoletaseletiva.service.HistoricoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Históricos", description = "Consulta do histórico de alterações das solicitações")
@RequestMapping("/historicos")
public class HistoricoController {
    private final HistoricoService historicoServ;

    public HistoricoController(HistoricoService historicoServ) {
        this.historicoServ = historicoServ;
    }

    @Operation(summary = "Lista o histórico de alterações com paginação e filtros",
            description = "Mostra situação anterior e nova, data, responsável, motivo e informações da tentativa. " +
                    "Filtros opcionais por solicitação e por operador, que podem ser combinados. Tamanho máximo de página: 100. " +
                    "O histórico é gerado automaticamente a cada mudança de situação, por isso só há consulta.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página de registros do histórico"),
            @ApiResponse(responseCode = "400", description = "Parâmetro de filtro ou paginação inválido",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Page<HistoricoResponseDTO>> listar(
            @Parameter(description = "Id do operador responsável pela alteração (opcional)")
            @RequestParam(required = false) Integer idOperador,
            @Parameter(description = "Id da solicitação (opcional)")
            @RequestParam(required = false) Integer idSolicitacao,
            Pageable pageable) {
        Pageable limitacaoFixa = PageRequest.of(
                pageable.getPageNumber(),
                Math.min(pageable.getPageSize(), 100),
                pageable.getSort()
        );
        return ResponseEntity.ok(historicoServ.listar(idOperador, idSolicitacao, limitacaoFixa));
    }
    @Operation(summary = "Busca um registro do histórico pelo id",
            description = "Retorna os dados de uma alteração de situação.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro encontrado"),
            @ApiResponse(responseCode = "404", description = "Registro do histórico não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<HistoricoResponseDTO> findById(
            @Parameter(description = "Id do registro do histórico")
            @PathVariable Integer id) {
        HistoricoResponseDTO historico = historicoServ.findById(id);
        return ResponseEntity.ok(historico);
    }
}
