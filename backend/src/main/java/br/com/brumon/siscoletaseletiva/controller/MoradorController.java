package br.com.brumon.siscoletaseletiva.controller;

import br.com.brumon.siscoletaseletiva.dto.request.MoradorRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.response.ApiErroResponse;
import br.com.brumon.siscoletaseletiva.dto.response.MoradorResponseDTO;
import br.com.brumon.siscoletaseletiva.service.MoradorService;
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
@Tag(name = "Moradores", description = "Cadastro e consulta de moradores (RF01)")
@RequestMapping("/moradores")
public class MoradorController {
    private final MoradorService moradorServ;
    public MoradorController(MoradorService moradorServ) {
        this.moradorServ = moradorServ;
    }
    @Operation(summary = "Cadastra um morador",
            description = "RF01. Exige nome e e-mail válido. O e-mail é normalizado (sem espaços e em minúsculas) antes de checar duplicidade. " +
                    "O telefone é opcional: a pontuação é removida e o prefixo 55 é acrescentado quando vier só com DDD e número.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Morador cadastrado"),
            @ApiResponse(responseCode = "400", description = "Entrada inválida: nome, e-mail ou telefone fora do formato",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Já existe morador com este e-mail",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @PostMapping
    public ResponseEntity<MoradorResponseDTO> criarMorador(@RequestBody @Valid MoradorRequestDTO dto) {
        MoradorResponseDTO moradorSalvo = moradorServ.salvarMorador(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(moradorSalvo);
    }
    @Operation(summary = "Lista moradores com paginação",
            description = "Filtro opcional por nome (contém o texto, sem diferenciar maiúsculas de minúsculas). Tamanho máximo de página: 100.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página de moradores"),
            @ApiResponse(responseCode = "400", description = "Parâmetro de paginação inválido",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Page<MoradorResponseDTO>> listar(
            //o sistema não vai dar erro se a pessoa não enviar nenhum nome.
            @Parameter(description = "Parte do nome para filtrar (opcional)")
            @RequestParam(required = false) String nome,
            @ParameterObject Pageable pageable) {
        Pageable limitacaoFixa = PageRequest.of(
                pageable.getPageNumber(),
                Math.min(pageable.getPageSize(), 100),
                pageable.getSort()
        );
        return ResponseEntity.ok(moradorServ.listar(nome, limitacaoFixa));
        //retorna uma lista paginada com todos os moradores cadastrados.
    }
    @Operation(summary = "Busca um morador pelo id",
            description = "Retorna os dados do morador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Morador encontrado"),
            @ApiResponse(responseCode = "404", description = "Morador não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<MoradorResponseDTO> findById(@PathVariable Integer id) {
        MoradorResponseDTO morador = moradorServ.findById(id);
        return ResponseEntity.ok(morador);
    }
    @Operation(summary = "Busca um morador pelo e-mail",
            description = "Retorna o morador cujo e-mail corresponde ao informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Morador encontrado"),
            @ApiResponse(responseCode = "400", description = "Parâmetro email ausente",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Morador não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @GetMapping("/por-email")
    public ResponseEntity<MoradorResponseDTO> findByEmail(@RequestParam String email) {
        MoradorResponseDTO morador = moradorServ.findByEmail(email);
        return ResponseEntity.ok(morador);
    }
    @Operation(summary = "Atualiza um morador",
            description = "RF01. Mesmas validações do cadastro. O e-mail só é rejeitado se já pertencer a outro morador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Morador atualizado"),
            @ApiResponse(responseCode = "400", description = "Entrada inválida",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Morador não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "E-mail já usado por outro morador",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<MoradorResponseDTO> atualizarMorador(
            @PathVariable Integer id,
            @RequestBody @Valid MoradorRequestDTO dto) {
        MoradorResponseDTO moradorAtualizado = moradorServ.atualizarMorador(dto, id);
        return ResponseEntity.ok(moradorAtualizado);
    }
    @Operation(summary = "Exclui um morador",
            description = "RN03. Só é possível excluir morador sem solicitações vinculadas, para preservar o histórico dos recolhimentos.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Morador excluído"),
            @ApiResponse(responseCode = "404", description = "Morador não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Morador possui solicitações vinculadas",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        moradorServ.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}