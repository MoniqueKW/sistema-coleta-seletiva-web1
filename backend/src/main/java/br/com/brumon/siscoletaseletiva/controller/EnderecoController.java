package br.com.brumon.siscoletaseletiva.controller;

import br.com.brumon.siscoletaseletiva.dto.request.EnderecoRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.request.EnderecoUpdateRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.response.ApiErroResponse;
import br.com.brumon.siscoletaseletiva.dto.response.EnderecoResponseDTO;
import br.com.brumon.siscoletaseletiva.service.EnderecoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@Tag(name = "Endereços", description = "Endereços vinculados a moradores")
@RequestMapping("/enderecos")
public class EnderecoController {

    private final EnderecoService enderecoService;
    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }
    @Operation(summary = "Cadastra um endereço para um morador",
            description = "Exige logradouro, número, bairro, município, UF (2 letras maiúsculas) e CEP. " +
                    "Quando o imóvel não tiver numeração, informe \"S/N\" no campo número. O complemento é opcional. " +
                    "Cada endereço pertence a um único morador, informado em idMorador.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Endereço cadastrado"),
            @ApiResponse(responseCode = "400", description = "Entrada inválida: campo obrigatório ausente ou fora do formato",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Morador não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @PostMapping
    public ResponseEntity<EnderecoResponseDTO> criarEndereco(@RequestBody @Valid EnderecoRequestDTO dto) {
        EnderecoResponseDTO enderecoCriado = enderecoService.salvarEndereco(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(enderecoCriado);
    }
    @Operation(summary = "Lista endereços com paginação e filtros",
            description = "Todos os filtros são opcionais e combinados (E). Morador e CEP são comparados de forma exata; " +
                    "UF, município, bairro e logradouro aceitam parte do texto, sem diferenciar maiúsculas de minúsculas. " +
                    "Tamanho máximo de página: 100.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página de endereços"),
            @ApiResponse(responseCode = "400", description = "Parâmetro de filtro ou paginação inválido",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Page<EnderecoResponseDTO>> listar(
            @Parameter(description = "Id do morador dono dos endereços (opcional)")
            @RequestParam(required = false) Integer idMorador,
            @Parameter(description = "CEP exato (opcional)")
            @RequestParam(required = false) String cep,
            @Parameter(description = "Parte da UF para filtrar (opcional)")
            @RequestParam(required = false) String uf,
            @Parameter(description = "Parte do município para filtrar (opcional)")
            @RequestParam(required = false) String municipio,
            @Parameter(description = "Parte do bairro para filtrar (opcional)")
            @RequestParam(required = false) String bairro,
            @Parameter(description = "Parte do logradouro para filtrar (opcional)")
            @RequestParam(required = false) String logradouro,
            Pageable pageable) {
        // Se o usuário pediu 20, usa 20. Se pediu 500, limita a 100.
        Pageable limitacaoFixa = PageRequest.of(
                pageable.getPageNumber(),
                Math.min(pageable.getPageSize(), 100),
                pageable.getSort()
        );
        //Para retornar todos os endereços paginados
        return ResponseEntity.ok(enderecoService.listar(idMorador, cep, uf, municipio, bairro, logradouro, limitacaoFixa));
    }
    @Operation(summary = "Busca um endereço pelo id",
            description = "Retorna os dados do endereço e o id do morador proprietário.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Endereço encontrado"),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> findById(@PathVariable Integer id) {
        EnderecoResponseDTO endereco = enderecoService.findById(id);
        return ResponseEntity.ok(endereco);
    }
    @Operation(summary = "Atualiza um endereço",
            description = "Mesmas validações do cadastro. O morador proprietário não pode ser alterado, por isso idMorador não faz parte do corpo. " +
                    "Solicitações já criadas não são afetadas, pois guardam uma cópia do endereço (RN04).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Endereço atualizado"),
            @ApiResponse(responseCode = "400", description = "Entrada inválida",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> atualizarEndereco(
            @PathVariable Integer id,
            @RequestBody @Valid EnderecoUpdateRequestDTO dto) {
        EnderecoResponseDTO enderecoAtualizado = enderecoService.atualizarEndereco(id, dto);
        return ResponseEntity.ok(enderecoAtualizado);
    }
    @Operation(summary = "Exclui um endereço",
            description = "Remove o endereço do cadastro do morador. Solicitações existentes não são afetadas, " +
                    "pois guardam uma cópia do endereço de atendimento (RN04).")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Endereço excluído"),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        enderecoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}