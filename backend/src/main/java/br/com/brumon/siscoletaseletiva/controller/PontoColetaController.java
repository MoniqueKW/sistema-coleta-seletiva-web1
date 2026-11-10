package br.com.brumon.siscoletaseletiva.controller;

import br.com.brumon.siscoletaseletiva.dto.request.AlterarSituacaoPontoRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.request.PontoColetaRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.response.ApiErroResponse;
import br.com.brumon.siscoletaseletiva.dto.response.PontoColetaResponseDTO;
import br.com.brumon.siscoletaseletiva.model.enums.SituacaoPonto;
import br.com.brumon.siscoletaseletiva.service.PontoColetaService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Pontos de coleta", description = "Cadastro e consulta de pontos de coleta")
@RequestMapping("/pontos")
public class PontoColetaController {
    private PontoColetaService pontoColetaServ;

    public PontoColetaController(PontoColetaService pontoColetaServ) {
        this.pontoColetaServ = pontoColetaServ;
    }
    @Operation(summary = "Cadastra um ponto de coleta",
            description = "Exige nome, endereço completo e se realiza recolhimento domiciliar. Contato e horários são opcionais.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ponto de coleta cadastrado"),
            @ApiResponse(responseCode = "400", description = "Entrada inválida: campo obrigatório ausente ou fora do formato",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @PostMapping
    public ResponseEntity<PontoColetaResponseDTO>salvarPontoColeta(@RequestBody @Valid PontoColetaRequestDTO dto) {
        PontoColetaResponseDTO pontoCriado = pontoColetaServ.salvarPontoColeta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pontoCriado);
    }
    @Operation(summary = "Lista pontos de coleta com paginação e filtros",
            description = "Todos os filtros são opcionais e combinados. Tamanho máximo de página: 100.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página de pontos de coleta"),
            @ApiResponse(responseCode = "400", description = "Parâmetro de filtro ou paginação inválido",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    //@RequestParam(required = false): permite que a requisição venha com ou sem filtros.
    @GetMapping
    public ResponseEntity<Page<PontoColetaResponseDTO>> listar(
            @Parameter(description = "Parte do nome para filtrar (opcional)")
            @RequestParam(required = false) String nome,
            @Parameter(description = "Parte do município para filtrar (opcional)")
            @RequestParam(required = false) String municipio,
            @Parameter(description = "Parte do bairro para filtrar (opcional)")
            @RequestParam(required = false) String bairro,
            @Parameter(description = "Situação do ponto: ATIVO ou INATIVO (opcional)")
            @RequestParam(required = false) SituacaoPonto situacao,
            @Parameter(description = "Filtra por pontos que fazem (true) ou não fazem (false) recolhimento domiciliar (opcional)")
            @RequestParam(required = false) Boolean recolhimentoDomiciliar,
            @Parameter(description = "Id do material aceito pelo ponto (opcional). Retorna só pontos ativos, salvo se a situação for informada")
            @RequestParam(required = false) Integer idMaterial,
            @ParameterObject Pageable pageable) {
        Pageable limitacaoFixa = PageRequest.of(
                pageable.getPageNumber(),
                Math.min(pageable.getPageSize(), 100),
                pageable.getSort()
        );
        return ResponseEntity.ok(pontoColetaServ.listar(nome, municipio, bairro, situacao, recolhimentoDomiciliar,
                idMaterial, limitacaoFixa));
    }
    @Operation(summary = "Busca um ponto de coleta pelo id",
            description = "Retorna os dados do ponto de coleta.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ponto de coleta encontrado"),
            @ApiResponse(responseCode = "404", description = "Ponto de coleta não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<PontoColetaResponseDTO> findById(
            @Parameter(description = "Id do ponto de coleta")
            @PathVariable Integer id) {
        PontoColetaResponseDTO ponto = pontoColetaServ.findById(id);
        return ResponseEntity.ok(ponto);
    }
    @Operation(summary = "Atualiza um ponto de coleta",
            description = "Mesmas validações do cadastro. A situação não é alterada aqui, use o endpoint de situação.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ponto de coleta atualizado"),
            @ApiResponse(responseCode = "400", description = "Entrada inválida",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ponto de coleta não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<PontoColetaResponseDTO> atualizarPontoColeta(
            @Parameter(description = "Id do ponto de coleta")
            @PathVariable Integer id,
            @RequestBody @Valid PontoColetaRequestDTO dto) {
        PontoColetaResponseDTO pontoAtualizado = pontoColetaServ.atualizarPontoColeta(id, dto);
        return ResponseEntity.ok(pontoAtualizado);
    }
    @Operation(summary = "Altera a situação de um ponto de coleta",
            description = "Ativa ou inativa o ponto. A inativação é bloqueada enquanto houver solicitações abertas vinculadas a ele.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Situação alterada"),
            @ApiResponse(responseCode = "400", description = "Situação ausente ou inválida",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ponto de coleta não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Existem solicitações abertas vinculadas ao ponto",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    //PATCH para altera apenas uma parte do recurso (a situação).
    @PatchMapping("/{id}/situacao")
    public ResponseEntity<PontoColetaResponseDTO> alterarSituacao(
            @Parameter(description = "Id do ponto de coleta")
            @PathVariable Integer id,
            @RequestBody @Valid AlterarSituacaoPontoRequestDTO situacaoDto) {
        PontoColetaResponseDTO pontoAtualizado = pontoColetaServ.alterarSituacao(id, situacaoDto);
        return ResponseEntity.ok(pontoAtualizado);
    }
    @Operation(summary = "Exclui um ponto de coleta",
            description = "Só é possível excluir ponto sem materiais associados e sem solicitações, para preservar o histórico. Nos demais casos, inative o ponto.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Ponto de coleta excluído"),
            @ApiResponse(responseCode = "404", description = "Ponto de coleta não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Ponto possui materiais associados ou solicitações",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    //caso seja criado um ponto de coleta errado e se perceba antes de associar materiais ou receber solicitações,
    // ele pode ser apagado para evitar um registo inútil no banco de dados.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "Id do ponto de coleta")
            @PathVariable Integer id) {
        pontoColetaServ.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}



