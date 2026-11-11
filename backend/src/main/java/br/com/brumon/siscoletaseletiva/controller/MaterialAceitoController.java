package br.com.brumon.siscoletaseletiva.controller;

import br.com.brumon.siscoletaseletiva.dto.request.MaterialAceitoRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.request.MaterialAceitoUpdateRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.response.ApiErroResponse;
import br.com.brumon.siscoletaseletiva.dto.response.MaterialAceitoResponseDTO;
import br.com.brumon.siscoletaseletiva.service.MaterialAceitoService;
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
@Tag(name = "Materiais aceitos", description = "Materiais aceitos em cada ponto de coleta (RF05, RN05)")
@RequestMapping("/pontos/{idPonto}/materiais")
public class MaterialAceitoController {
    private final MaterialAceitoService materialAceitoServ;

    public MaterialAceitoController(MaterialAceitoService materialAceitoServ) {
        this.materialAceitoServ = materialAceitoServ;
    }

    @Operation(summary = "Vincula um material a um ponto de coleta",
            description = "RF05. Associa um material ativo ao ponto informado na URL. Observações e limite por solicitação são opcionais; " +
                    "o limite, se informado, deve ser maior que zero. O mesmo material não pode ser vinculado duas vezes ao mesmo ponto.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Material vinculado ao ponto"),
            @ApiResponse(responseCode = "400", description = "Entrada inválida: material ausente ou limite menor ou igual a zero",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ponto de coleta ou material não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Material já vinculado ao ponto ou material inativo",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @PostMapping
    public ResponseEntity<MaterialAceitoResponseDTO> associarMaterial(
            @Parameter(description = "Id do ponto de coleta")
            @PathVariable Integer idPonto,
            @RequestBody @Valid MaterialAceitoRequestDTO dto) {
        MaterialAceitoResponseDTO associacaoCriada = materialAceitoServ.associarMaterial(idPonto, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(associacaoCriada);
    }
    @Operation(summary = "Lista os materiais aceitos por um ponto de coleta",
            description = "RF06. Retorna os materiais vinculados ao ponto, com observações e limite. Tamanho máximo de página: 100.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página de materiais aceitos"),
            @ApiResponse(responseCode = "400", description = "Parâmetro de paginação inválido",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    //usando o findByIdPonto como findAll pq as consultas sempre vão ser em
    //relação a um ponto (materiais aceitos de um ponto)
    @GetMapping
    public ResponseEntity<Page<MaterialAceitoResponseDTO>> listar(
            @Parameter(description = "Id do ponto de coleta")
            @PathVariable Integer idPonto,
            @ParameterObject Pageable pageable) {
        Pageable limitacaoFixa = PageRequest.of(
                pageable.getPageNumber(),
                Math.min(pageable.getPageSize(), 100),
                pageable.getSort()
        );
        return ResponseEntity.ok(materialAceitoServ.findByIdPonto(idPonto, limitacaoFixa));
    }

    @Operation(summary = "Atualiza o vínculo de um material com o ponto",
            description = "RF05. Altera observações e limite por solicitação. O material do vínculo não pode ser trocado; para isso, remova o vínculo e crie outro. " +
                    "O limite, se informado, deve ser maior que zero.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vínculo atualizado"),
            @ApiResponse(responseCode = "400", description = "Entrada inválida: limite menor ou igual a zero",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Vínculo entre o ponto e o material não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @PutMapping("/{idMaterial}")
    public ResponseEntity<MaterialAceitoResponseDTO> atualizarAssociacao(
            @Parameter(description = "Id do ponto de coleta")
            @PathVariable Integer idPonto,
            @Parameter(description = "Id do material vinculado")
            @PathVariable Integer idMaterial,
            @RequestBody @Valid MaterialAceitoUpdateRequestDTO dto) {
        MaterialAceitoResponseDTO associacaoAtualizada = materialAceitoServ.atualizarMaterialAceito(idPonto, idMaterial, dto);
        return ResponseEntity.ok(associacaoAtualizada);
    }
    @Operation(summary = "Remove o vínculo de um material com o ponto",
            description = "RN05. A remoção é bloqueada enquanto houver solicitações em andamento com esse material no ponto. " +
                    "O operador deve resolver ou cancelar esses atendimentos antes.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Vínculo removido"),
            @ApiResponse(responseCode = "404", description = "Vínculo entre o ponto e o material não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Existem solicitações em andamento com este material no ponto",
                    content = @Content(schema = @Schema(implementation = ApiErroResponse.class)))
    })
    @DeleteMapping("/{idMaterial}")
    public ResponseEntity<Void> deleteAssociacao(
            @Parameter(description = "Id do ponto de coleta")
            @PathVariable Integer idPonto,
            @Parameter(description = "Id do material vinculado")
            @PathVariable Integer idMaterial) {
        materialAceitoServ.deleteAssociacao(idPonto, idMaterial);
        return ResponseEntity.noContent().build();
    }
}