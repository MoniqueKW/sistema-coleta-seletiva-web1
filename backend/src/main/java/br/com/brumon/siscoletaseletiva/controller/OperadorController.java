package br.com.brumon.siscoletaseletiva.controller;

import br.com.brumon.siscoletaseletiva.dto.request.OperadorRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.response.ApiErroResponse;
import br.com.brumon.siscoletaseletiva.dto.response.OperadorResponseDTO;
import br.com.brumon.siscoletaseletiva.service.OperadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Operadores", description = "Operadores vinculados a pontos de coleta")
@RequestMapping("/pontos/{idPonto}/operadores")
public class OperadorController {
    private final OperadorService operadorServ;
    public OperadorController(OperadorService operadorServ) {
        this.operadorServ = operadorServ;
    }
    @Operation(summary = "Cadastra um operador em um ponto de coleta",
            description = "Cria o operador já vinculado ao ponto informado na URL. O e-mail é normalizado (sem espaços e em minúsculas) e não pode repetir.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Operador cadastrado"),
            @ApiResponse(responseCode = "400", description = "Entrada inválida: nome ou e-mail fora do formato",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ponto de coleta não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Já existe operador com este e-mail",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @PostMapping
    public ResponseEntity<OperadorResponseDTO> criarOperador(
            @Parameter(description = "Id do ponto de coleta")
            @PathVariable Integer idPonto,
            @RequestBody @Valid OperadorRequestDTO dto) {
        OperadorResponseDTO operadorCriado = operadorServ.salvarOperador(idPonto, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(operadorCriado);
    }
    @Operation(summary = "Lista os operadores de um ponto de coleta",
            description = "Filtro opcional por nome (contém o texto, sem diferenciar maiúsculas de minúsculas). Tamanho máximo de página: 100.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página de operadores"),
            @ApiResponse(responseCode = "400", description = "Parâmetro inválido",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Page<OperadorResponseDTO>> listar(
            @Parameter(description = "Id do ponto de coleta")
            @PathVariable Integer idPonto,
            @Parameter(description = "Parte do nome para filtrar (opcional)")
            @RequestParam(required = false) String nome,
            Pageable pageable) {
        Pageable limitacaoFixa = PageRequest.of(
                pageable.getPageNumber(),
                Math.min(pageable.getPageSize(), 100),
                pageable.getSort()
        );
        //Para filtrar caso tenha o id pontocoleta, nesse caso simples dava para
        //fazer aqui mesmo, mas para padronizar melhor manter o metodo filtrar no service
        return ResponseEntity.ok(operadorServ.listar(idPonto, nome, limitacaoFixa));
    }
    @Operation(summary = "Busca um operador pelo id",
            description = "Retorna os dados do operador.")
    @Parameter(name = "idPonto", in = ParameterIn.PATH, required = true,
            description = "Id do ponto de coleta na URL (esta operação não o usa para filtrar)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operador encontrado"),
            @ApiResponse(responseCode = "404", description = "Operador não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<OperadorResponseDTO> findById(@Parameter(description = "Id do operador") @PathVariable Integer id) {
        OperadorResponseDTO operador = operadorServ.findById(id);
        return ResponseEntity.ok(operador);
    }
    @Operation(summary = "Busca um operador pelo e-mail",
            description = "Retorna o operador cujo e-mail corresponde ao informado.")
    @Parameter(name = "idPonto", in = ParameterIn.PATH, required = true,
            description = "Id do ponto de coleta na URL (esta operação não o usa para filtrar)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operador encontrado"),
            @ApiResponse(responseCode = "400", description = "Parâmetro email ausente",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Operador não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @GetMapping("/por-email")
    public ResponseEntity<OperadorResponseDTO> findByEmail(@Parameter(description = "E-mail do operador") @RequestParam String email) {
        OperadorResponseDTO operador = operadorServ.findByEmail(email);
        return ResponseEntity.ok(operador);
    }
    @Operation(summary = "Atualiza um operador",
            description = "Atualiza nome e e-mail e vincula o operador ao ponto informado na URL. O e-mail só é rejeitado se já pertencer a outro operador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operador atualizado"),
            @ApiResponse(responseCode = "400", description = "Entrada inválida",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Operador ou ponto de coleta não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "E-mail já usado por outro operador",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<OperadorResponseDTO> atualizarOperador(
            @Parameter(description = "Id do ponto de coleta")
            @PathVariable Integer idPonto,
            @Parameter(description = "Id do operador")
            @PathVariable Integer id,
            @RequestBody @Valid OperadorRequestDTO dto) {
        OperadorResponseDTO operadorAtualizado = operadorServ.atualizarOperador(idPonto, id, dto);
        return ResponseEntity.ok(operadorAtualizado);
    }
    @Operation(summary = "Exclui um operador",
            description = "Só é possível excluir operador sem registros no histórico de solicitações, para preservar quem fez cada alteração.")
    @Parameter(name = "idPonto", in = ParameterIn.PATH, required = true,
            description = "Id do ponto de coleta na URL (esta operação não o usa para filtrar)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Operador excluído"),
            @ApiResponse(responseCode = "404", description = "Operador não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Operador possui registros no histórico",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "Id do operador") @PathVariable Integer id) {
        operadorServ.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}