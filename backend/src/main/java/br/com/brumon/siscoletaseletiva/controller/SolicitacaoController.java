package br.com.brumon.siscoletaseletiva.controller;

import br.com.brumon.siscoletaseletiva.dto.request.AgendamentoRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.request.ConclusaoRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.request.SolicitacaoRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.request.SolicitacaoUpdateRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.response.ApiErroResponse;
import br.com.brumon.siscoletaseletiva.dto.response.ItemSolicitacaoResponseDTO;
import br.com.brumon.siscoletaseletiva.dto.response.SolicitacaoResponseDTO;
import br.com.brumon.siscoletaseletiva.model.enums.SituacaoSolicitacao;
import br.com.brumon.siscoletaseletiva.service.SolicitacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@Tag(name = "Solicitações", description = "Solicitações de recolhimento, agendamento e execução (RF08 a RF14)")
@RequestMapping("/solicitacoes")
public class SolicitacaoController {
    private final SolicitacaoService solicitacaoServ;
    public SolicitacaoController(SolicitacaoService solicitacaoServ) {
        this.solicitacaoServ = solicitacaoServ;
    }
    @Operation(summary = "Lista solicitações com paginação e filtros",
            description = "RF09. Todos os filtros são opcionais e combinados. Datas no formato ISO com fuso (ex: 2026-11-10T08:00:00-03:00). Tamanho máximo de página: 100.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página de solicitações"),
            @ApiResponse(responseCode = "400", description = "Parâmetro de filtro ou paginação inválido",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Page<SolicitacaoResponseDTO>> listar(
            @Parameter(description = "Id do morador (opcional)")
            @RequestParam(required = false) Integer idMorador,
            @Parameter(description = "Id do ponto de coleta (opcional)")
            @RequestParam(required = false) Integer idPonto,
            @Parameter(description = "Situação da solicitação (opcional)")
            @RequestParam(required = false) SituacaoSolicitacao situacao,
            @Parameter(description = "Data de criação inicial (opcional)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dataInicio,
            @Parameter(description = "Data de criação final (opcional)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dataFim,
            @ParameterObject Pageable pageable) {
        Pageable limitacaoFixa = PageRequest.of(
                pageable.getPageNumber(),
                Math.min(pageable.getPageSize(), 100),
                pageable.getSort()
        );
        return ResponseEntity.ok(solicitacaoServ.listar(idMorador, idPonto, situacao,
                dataInicio, dataFim, limitacaoFixa));
    }
    @Operation(summary = "Busca uma solicitação pelo id",
            description = "RF09. Retorna os dados da solicitação.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitação encontrada"),
            @ApiResponse(responseCode = "404", description = "Solicitação não encontrada",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoResponseDTO> findById(
            @Parameter(description = "Id da solicitação")
            @PathVariable Integer id) {
        SolicitacaoResponseDTO solicitacao = solicitacaoServ.findById(id);
        return ResponseEntity.ok(solicitacao);
    }
    @Operation(summary = "Lista os itens de uma solicitação",
            description = "Retorna os materiais, a unidade e as quantidades estimada e efetiva de cada item.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Itens da solicitação"),
            @ApiResponse(responseCode = "404", description = "Solicitação não encontrada",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @GetMapping("/{id}/itens")
    public ResponseEntity<List<ItemSolicitacaoResponseDTO>> findItensBySolicitacaoId(
            @Parameter(description = "Id da solicitação")
            @PathVariable Integer id) {
        List<ItemSolicitacaoResponseDTO> itens = solicitacaoServ.findItensBySolicitacaoId(id);
        return ResponseEntity.ok(itens);
    }
    @Operation(summary = "Busca uma solicitação pelo protocolo",
            description = "Retorna a solicitação cujo protocolo corresponde ao informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitação encontrada"),
            @ApiResponse(responseCode = "404", description = "Solicitação não encontrada",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @GetMapping("/protocolo/{protocolo}")
    public ResponseEntity<SolicitacaoResponseDTO> findByProtocolo(
            @Parameter(description = "Protocolo da solicitação")
            @PathVariable String protocolo) {
        SolicitacaoResponseDTO solicitacao = solicitacaoServ.findByProtocoloUnico(protocolo);
        return ResponseEntity.ok(solicitacao);
    }
    @Operation(summary = "Cria uma solicitação de recolhimento",
            description = "RF08 e RN01. Gera o protocolo e registra a situação SOLICITADA. O ponto precisa estar ativo e com recolhimento domiciliar, " +
                    "e todos os materiais precisam estar ativos e aceitos pelo ponto, sem repetição e dentro do limite. " +
                    "Se algo falhar, nada é gravado. O idOperador é opcional (use quando o operador criar pelo morador).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Solicitação criada"),
            @ApiResponse(responseCode = "400", description = "Entrada inválida",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Morador, ponto, material ou operador não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Regra de negócio violada (ponto inativo, material não aceito, limite excedido, etc.)",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @PostMapping
    public ResponseEntity<SolicitacaoResponseDTO> criarSolicitacao(
            @RequestBody @Valid SolicitacaoRequestDTO dto,
            @Parameter(description = "Id do operador responsável pelo registro (opcional)")
            @RequestParam(required = false) Integer idOperador) {
        SolicitacaoResponseDTO solicitacaoCriada = solicitacaoServ.salvarSolicitacao(dto, idOperador);
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitacaoCriada);
    }
    @Operation(summary = "Atualiza uma solicitação",
            description = "RN04. Só é possível enquanto a situação for SOLICITADA. Permite corrigir ponto, endereço, observações e itens, com nova validação. " +
                    "É preciso enviar a versão atual: se outra alteração aconteceu antes, retorna conflito (RN06).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitação atualizada"),
            @ApiResponse(responseCode = "400", description = "Entrada inválida",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Solicitação, ponto, endereço, material ou operador não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Solicitação fora da situação SOLICITADA, versão desatualizada ou regra de compatibilidade violada",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    //esse recebe um dto so pra ele, pois nem os ids e a solicitacao podem ser alterados,
    //ent fiz um dto so pra update da solicitacao, sem esses dados
    @PatchMapping("/{id}")
    public ResponseEntity<SolicitacaoResponseDTO> atualizarSolicitacao(
            @Parameter(description = "Id da solicitação")
            @PathVariable Integer id,
            @Parameter(description = "Id do operador (opcional)")
            @RequestParam(required = false) Integer idOperador,
            @RequestBody @Valid SolicitacaoUpdateRequestDTO dto) {
        SolicitacaoResponseDTO solicitacaoAtualizada = solicitacaoServ.atualizarSolicitacao(id, idOperador, dto);
        return ResponseEntity.ok(solicitacaoAtualizada);
    }
    @Operation(summary = "Agenda uma solicitação",
            description = "RF10. Só vale para solicitação SOLICITADA. A data e hora precisam ser futuras. Altera a situação para AGENDADA e registra no histórico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitação agendada"),
            @ApiResponse(responseCode = "400", description = "Data ausente ou no passado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Solicitação ou operador não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Solicitação não está SOLICITADA ou operador não pertence ao ponto",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @PatchMapping("/{id}/agendar")
    public ResponseEntity<SolicitacaoResponseDTO> agendarSolicitacao(
            @Parameter(description = "Id da solicitação")
            @PathVariable Integer id,
            @Parameter(description = "Id do operador do ponto")
            @RequestParam Integer idOperador,
            @RequestBody @Valid AgendamentoRequestDTO dto) {
        SolicitacaoResponseDTO solicitacaoAgendada = solicitacaoServ.agendarSolicitacao(id, dto, idOperador);
        return ResponseEntity.ok(solicitacaoAgendada);
    }
    @Operation(summary = "Recusa uma solicitação",
            description = "RF11. Só vale para solicitação SOLICITADA e exige motivo. A recusa é uma situação final e fica no histórico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitação recusada"),
            @ApiResponse(responseCode = "404", description = "Solicitação ou operador não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Solicitação não está SOLICITADA, motivo em branco ou operador não pertence ao ponto",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @PatchMapping("/{id}/recusar")
    public ResponseEntity<SolicitacaoResponseDTO> recusarSolicitacao(
            @Parameter(description = "Id da solicitação")
            @PathVariable Integer id,
            @Parameter(description = "Id do operador do ponto")
            @RequestParam Integer idOperador,
            @Parameter(description = "Justificativa da recusa")
            @RequestParam String motivo) {
        SolicitacaoResponseDTO solicitacaoRecusada = solicitacaoServ.recusarSolicitacao(id, motivo, idOperador);
        return ResponseEntity.ok(solicitacaoRecusada);
    }
    @Operation(summary = "Inicia o recolhimento",
            description = "RF13. Só vale para solicitação AGENDADA. Altera a situação para EM_RECOLHIMENTO.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recolhimento iniciado"),
            @ApiResponse(responseCode = "404", description = "Solicitação ou operador não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Solicitação não está AGENDADA ou operador não pertence ao ponto",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @PatchMapping("/{id}/iniciar-recolhimento")
    public ResponseEntity<SolicitacaoResponseDTO> iniciarRecolhimento(
            @Parameter(description = "Id da solicitação")
            @PathVariable Integer id,
            @Parameter(description = "Id do operador do ponto")
            @RequestParam Integer idOperador) {
        SolicitacaoResponseDTO solicitacao = solicitacaoServ.iniciarRecolhimento(id, idOperador);
        return ResponseEntity.ok(solicitacao);
    }
    @Operation(summary = "Cancela uma solicitação",
            description = "RF12. Só vale para solicitação SOLICITADA ou AGENDADA e exige motivo. O idOperador é opcional (o morador também pode cancelar).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitação cancelada"),
            @ApiResponse(responseCode = "404", description = "Solicitação ou operador não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Solicitação já iniciada ou finalizada, motivo em branco ou operador não pertence ao ponto",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<SolicitacaoResponseDTO> cancelarSolicitacao(
            @Parameter(description = "Id da solicitação")
            @PathVariable Integer id,
            @Parameter(description = "Id do operador do ponto (opcional)")
            @RequestParam(required = false) Integer idOperador,
            @Parameter(description = "Motivo do cancelamento")
            @RequestParam String motivo) {
        SolicitacaoResponseDTO solicitacaoCancelada = solicitacaoServ.cancelarSolicitacao(id, motivo, idOperador);
        return ResponseEntity.ok(solicitacaoCancelada);
    }
    @Operation(summary = "Conclui uma solicitação",
            description = "RF13. Só vale para solicitação EM_RECOLHIMENTO. Informe a quantidade efetiva de todos os itens (zero para item não recolhido), " +
                    "com pelo menos um item positivo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitação concluída"),
            @ApiResponse(responseCode = "400", description = "Entrada inválida",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Solicitação ou operador não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Solicitação não está EM_RECOLHIMENTO, quantidades inválidas ou operador não pertence ao ponto",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<SolicitacaoResponseDTO> concluirSolicitacao(
            @Parameter(description = "Id da solicitação")
            @PathVariable Integer id,
            @Parameter(description = "Id do operador do ponto")
            @RequestParam Integer idOperador,
            @RequestBody @Valid ConclusaoRequestDTO dto) {
        SolicitacaoResponseDTO solicitacaoConcluida = solicitacaoServ.concluirSolicitacao(id, dto, idOperador);
        return ResponseEntity.ok(solicitacaoConcluida);
    }
    @Operation(summary = "Reagenda uma solicitação",
            description = "RF14. Vale para solicitação AGENDADA ou EM_RECOLHIMENTO (tentativa sem sucesso). Exige justificativa, informação da tentativa e nova data futura. " +
                    "A situação volta para AGENDADA e a tentativa anterior fica no histórico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitação reagendada"),
            @ApiResponse(responseCode = "400", description = "Data ausente ou no passado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Solicitação ou operador não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Situação inválida, justificativa ou tentativa em branco, ou operador não pertence ao ponto",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @PatchMapping("/{id}/reagendar")
    public ResponseEntity<SolicitacaoResponseDTO> reagendarSolicitacao(
            @Parameter(description = "Id da solicitação")
            @PathVariable Integer id,
            @Parameter(description = "Id do operador do ponto")
            @RequestParam Integer idOperador,
            @Parameter(description = "Justificativa do reagendamento")
            @RequestParam String motivo,
            @Parameter(description = "Informações da tentativa sem sucesso")
            @RequestParam String infoTentativa,
            @RequestBody @Valid AgendamentoRequestDTO dto) {
        SolicitacaoResponseDTO solicitacaoReagendada = solicitacaoServ.reagendarSolicitacao(id, motivo, infoTentativa,
                dto, idOperador);
        return ResponseEntity.ok(solicitacaoReagendada);
    }
}
