package br.com.brumon.siscoletaseletiva.Controller;

import br.com.brumon.siscoletaseletiva.DTO.Request.AlterarSituacaoMaterialRequestDTO;
import br.com.brumon.siscoletaseletiva.DTO.Request.MaterialRequestDTO;
import br.com.brumon.siscoletaseletiva.DTO.Response.MaterialResponseDTO;
import br.com.brumon.siscoletaseletiva.Enum.SituacaoMaterial;
import br.com.brumon.siscoletaseletiva.Service.MaterialService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/materiais")
public class MaterialController {

    private final MaterialService materialServ;

    public MaterialController(MaterialService materialServ) {
        this.materialServ = materialServ;
    }

    @PostMapping
    public ResponseEntity<MaterialResponseDTO> criarMaterial(@RequestBody @Valid MaterialRequestDTO dto) {
        MaterialResponseDTO materialCriado = materialServ.salvarMaterial(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(materialCriado);
    }

    @GetMapping
    public ResponseEntity<Page<MaterialResponseDTO>> listarMateriais(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) SituacaoMaterial situacao,
            Pageable pageable) {

        //Verifica qual filtro foi enviado
        if (nome != null && !nome.isBlank()) {
            return ResponseEntity.ok(materialServ.findByNome(nome, pageable));
        } else if (situacao != null) {
            return ResponseEntity.ok(materialServ.findBySituacao(situacao, pageable));
        }

        //Para lista todos os materiais paginados
        return ResponseEntity.ok(materialServ.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialResponseDTO> buscarPorId(@PathVariable Integer id) {
        MaterialResponseDTO material = materialServ.findById(id);
        return ResponseEntity.ok(material);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaterialResponseDTO> atualizarMaterial(
            @PathVariable Integer id,
            @RequestBody @Valid MaterialRequestDTO dto) {
        MaterialResponseDTO materialAtualizado = materialServ.atualizarMaterial(dto, id);
        return ResponseEntity.ok(materialAtualizado);
    }

    @PatchMapping("/{id}/situacao")
    public ResponseEntity<MaterialResponseDTO> alterarSituacao(
            @PathVariable Integer id,
            @RequestBody @Valid AlterarSituacaoMaterialRequestDTO situacaoDto) {
        MaterialResponseDTO materialAtualizado = materialServ.alterarSituacao(id, situacaoDto);
        return ResponseEntity.ok(materialAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagarMaterial(@PathVariable Integer id) {
        materialServ.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}