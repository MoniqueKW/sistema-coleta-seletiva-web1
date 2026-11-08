package br.com.brumon.siscoletaseletiva.controller;

import br.com.brumon.siscoletaseletiva.dto.request.AlterarSituacaoPontoRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.request.PontoColetaRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.response.PontoColetaResponseDTO;
import br.com.brumon.siscoletaseletiva.model.enums.SituacaoPonto;
import br.com.brumon.siscoletaseletiva.service.PontoColetaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pontos")
public class PontoColetaController {
    private PontoColetaService pontoColetaServ;

    public PontoColetaController(PontoColetaService pontoColetaServ) {
        this.pontoColetaServ = pontoColetaServ;
    }

    @PostMapping
    public ResponseEntity<PontoColetaResponseDTO>salvarPontoColeta(@RequestBody @Valid PontoColetaRequestDTO dto) {
        PontoColetaResponseDTO pontoCriado = pontoColetaServ.salvarPontoColeta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pontoCriado);
    }

    //@RequestParam(required = false): permite que a requisição venha com ou sem filtros.
    @GetMapping
    public ResponseEntity<Page<PontoColetaResponseDTO>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String municipio,
            @RequestParam(required = false) String bairro,
            @RequestParam(required = false) SituacaoPonto situacao,
            @RequestParam(required = false) Boolean recolhimentoDomiciliar,
            Pageable pageable) {
        Pageable limitacaoFixa = PageRequest.of(
                pageable.getPageNumber(),
                Math.min(pageable.getPageSize(), 100),
                pageable.getSort()
        );
        return ResponseEntity.ok(pontoColetaServ.listar(nome, municipio, bairro, situacao, recolhimentoDomiciliar,
                limitacaoFixa));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PontoColetaResponseDTO> findById(@PathVariable Integer id) {
        PontoColetaResponseDTO ponto = pontoColetaServ.findById(id);
        return ResponseEntity.ok(ponto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PontoColetaResponseDTO> atualizarPontoColeta(
            @PathVariable Integer id,
            @RequestBody @Valid PontoColetaRequestDTO dto) {
        PontoColetaResponseDTO pontoAtualizado = pontoColetaServ.atualizarPontoColeta(id, dto);
        return ResponseEntity.ok(pontoAtualizado);
    }

    //PATCH para altera apenas uma parte do recurso (a situação).
    @PatchMapping("/{id}/situacao")
    public ResponseEntity<PontoColetaResponseDTO> alterarSituacao(
            @PathVariable Integer id,
            @RequestBody @Valid AlterarSituacaoPontoRequestDTO situacaoDto) {
        PontoColetaResponseDTO pontoAtualizado = pontoColetaServ.alterarSituacao(id, situacaoDto);
        return ResponseEntity.ok(pontoAtualizado);
    }

    //caso seja criado um ponto de coleta errado e se perceba antes de associar materiais ou receber solicitações,
    // ele pode ser apagado para evitar um registo inútil no banco de dados.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        pontoColetaServ.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}



