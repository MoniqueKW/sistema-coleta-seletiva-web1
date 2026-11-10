package br.com.brumon.siscoletaseletiva.controller;

import br.com.brumon.siscoletaseletiva.dto.request.AlterarSituacaoMaterialRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.request.MaterialRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.response.ApiErroResponse;
import br.com.brumon.siscoletaseletiva.dto.response.MaterialResponseDTO;
import br.com.brumon.siscoletaseletiva.model.enums.SituacaoMaterial;
import br.com.brumon.siscoletaseletiva.service.MaterialService;
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
@Tag(name = "Materiais", description = "Cadastro e consulta de materiais recicláveis")
@RequestMapping("/materiais")
public class MaterialController {
    private final MaterialService materialServ;
    public MaterialController(MaterialService materialServ) {
        this.materialServ = materialServ;
    }
    @Operation(summary = "Cadastra um material reciclável",
            description = "Exige nome e unidade de medida. Descrição e orientação de preparo são opcionais. O nome não pode repetir.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Material cadastrado"),
            @ApiResponse(responseCode = "400", description = "Entrada inválida: nome ou unidade ausente ou fora do formato",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Já existe material com este nome",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @PostMapping
    public ResponseEntity<MaterialResponseDTO> criarMaterial(@RequestBody @Valid MaterialRequestDTO dto) {
        MaterialResponseDTO materialCriado = materialServ.salvarMaterial(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(materialCriado);
    }
    @Operation(summary = "Lista materiais com paginação e filtros",
            description = "Filtros opcionais por nome (contém o texto, sem diferenciar maiúsculas de minúsculas) e por situação (ATIVO ou INATIVO). Tamanho máximo de página: 100.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página de materiais"),
            @ApiResponse(responseCode = "400", description = "Parâmetro de filtro ou paginação inválido",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Page<MaterialResponseDTO>> listar(
            @Parameter(description = "Parte do nome para filtrar (opcional)")
            @RequestParam(required = false) String nome,
            @Parameter(description = "Situação do material: ATIVO ou INATIVO (opcional)")
            @RequestParam(required = false) SituacaoMaterial situacao,
            @ParameterObject Pageable pageable) {
        Pageable limitacaoFixa = PageRequest.of(
                pageable.getPageNumber(),
                Math.min(pageable.getPageSize(), 100),
                pageable.getSort()
        );
        //Verifica qual filtro foi enviado no metodo filtrar no service de material
        return ResponseEntity.ok(materialServ.listar(nome, situacao, limitacaoFixa));
    }

    @Operation(summary = "Busca um material pelo id",
            description = "Retorna os dados do material.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Material encontrado"),
            @ApiResponse(responseCode = "404", description = "Material não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<MaterialResponseDTO> findById(
            @Parameter(description = "Id do material")
            @PathVariable Integer id) {
        MaterialResponseDTO material = materialServ.findById(id);
        return ResponseEntity.ok(material);
    }
    @Operation(summary = "Atualiza um material",
            description = "Mesmas validações do cadastro. O nome só é rejeitado se já pertencer a outro material. A situação não é alterada aqui, use o endpoint de situação.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Material atualizado"),
            @ApiResponse(responseCode = "400", description = "Entrada inválida",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Material não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Nome já usado por outro material",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<MaterialResponseDTO> atualizarMaterial(
            @Parameter(description = "Id do material")
            @PathVariable Integer id,
            @RequestBody @Valid MaterialRequestDTO dto) {
        MaterialResponseDTO materialAtualizado = materialServ.atualizarMaterial(dto, id);
        return ResponseEntity.ok(materialAtualizado);
    }
    @Operation(summary = "Altera a situação de um material",
            description = "Ativa ou inativa o material. Material inativo não pode entrar em novas solicitações, mas os registros existentes são preservados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Situação alterada"),
            @ApiResponse(responseCode = "400", description = "Situação ausente ou inválida",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Material não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @PatchMapping("/{id}/situacao")
    public ResponseEntity<MaterialResponseDTO> alterarSituacao(
            @Parameter(description = "Id do material")
            @PathVariable Integer id,
            @RequestBody @Valid AlterarSituacaoMaterialRequestDTO situacaoDto) {
        MaterialResponseDTO materialAtualizado = materialServ.alterarSituacao(id, situacaoDto);
        return ResponseEntity.ok(materialAtualizado);
    }

    @Operation(summary = "Exclui um material",
            description = "Só é possível excluir material sem vínculos com pontos e sem solicitações, para preservar o histórico. Nos demais casos, inative o material.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Material excluído"),
            @ApiResponse(responseCode = "404", description = "Material não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Material possui vínculos com pontos ou solicitações",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "Id do material")
            @PathVariable Integer id) {
        materialServ.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}