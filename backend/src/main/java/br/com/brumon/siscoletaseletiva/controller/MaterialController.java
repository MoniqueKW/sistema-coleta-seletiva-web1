package br.com.brumon.siscoletaseletiva.controller;

import br.com.brumon.siscoletaseletiva.dto.request.AlterarSituacaoMaterialRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.request.MaterialRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.response.MaterialResponseDTO;
import br.com.brumon.siscoletaseletiva.model.enums.SituacaoMaterial;
import br.com.brumon.siscoletaseletiva.service.MaterialService;
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
    public ResponseEntity<Page<MaterialResponseDTO>> findAll(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) SituacaoMaterial situacao,
            Pageable pageable) {
        //Verifica qual filtro foi enviado no metodo filtrar no service de material
        return ResponseEntity.ok(materialServ.filtrar(nome, situacao, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialResponseDTO> findById(@PathVariable Integer id) {
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
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        materialServ.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}