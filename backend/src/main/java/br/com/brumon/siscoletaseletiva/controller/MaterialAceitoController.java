package br.com.brumon.siscoletaseletiva.controller;

import br.com.brumon.siscoletaseletiva.dto.request.MaterialAceitoRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.request.MaterialAceitoUpdateRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.response.MaterialAceitoResponseDTO;
import br.com.brumon.siscoletaseletiva.service.MaterialAceitoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pontos/{idPonto}/materiais")
public class MaterialAceitoController {

    private final MaterialAceitoService materialAceitoServ;

    public MaterialAceitoController(MaterialAceitoService materialAceitoServ) {
        this.materialAceitoServ = materialAceitoServ;
    }

    @PostMapping
    public ResponseEntity<MaterialAceitoResponseDTO> associarMaterial(
            @PathVariable Integer idPonto,
            @RequestBody @Valid MaterialAceitoRequestDTO dto) {
        MaterialAceitoResponseDTO associacaoCriada = materialAceitoServ.associarMaterial(idPonto, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(associacaoCriada);
    }
    //usando o findByIdPonto como findAll pq as consultas sempre vão ser em
    //relação a um ponto (materiais aceitos de um ponto)
    @GetMapping
    public ResponseEntity<Page<MaterialAceitoResponseDTO>> findAll(
            @PathVariable Integer idPonto,
            Pageable pageable) {
        return ResponseEntity.ok(materialAceitoServ.findByIdPonto(idPonto, pageable));
    }

    @PutMapping("/{idMaterial}")
    public ResponseEntity<MaterialAceitoResponseDTO> atualizarAssociacao(
            @PathVariable Integer idPonto,
            @PathVariable Integer idMaterial,
            @RequestBody @Valid MaterialAceitoUpdateRequestDTO dto) {
        MaterialAceitoResponseDTO associacaoAtualizada = materialAceitoServ.atualizarMaterialAceito(idPonto, idMaterial, dto);
        return ResponseEntity.ok(associacaoAtualizada);
    }

    @DeleteMapping("/{idMaterial}")
    public ResponseEntity<Void> deleteAssociacao(
            @PathVariable Integer idPonto,
            @PathVariable Integer idMaterial) {
        materialAceitoServ.deleteAssociacao(idPonto, idMaterial);
        return ResponseEntity.noContent().build();
    }
}