package br.com.brumon.siscoletaseletiva.Controller;

import br.com.brumon.siscoletaseletiva.DTO.Request.MaterialAceitoRequestDTO;
import br.com.brumon.siscoletaseletiva.DTO.Request.MaterialAceitoUpdateRequestDTO;
import br.com.brumon.siscoletaseletiva.DTO.Response.MaterialAceitoResponseDTO;
import br.com.brumon.siscoletaseletiva.Service.MaterialAceitoService;
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

    @GetMapping
    public ResponseEntity<Page<MaterialAceitoResponseDTO>> listarMateriaisDoPonto(
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
    public ResponseEntity<Void> removerAssociacao(
            @PathVariable Integer idPonto,
            @PathVariable Integer idMaterial) {
        materialAceitoServ.deleteByPontoColeta_IdAndMaterial_Id(idPonto, idMaterial);
        return ResponseEntity.noContent().build();
    }
}