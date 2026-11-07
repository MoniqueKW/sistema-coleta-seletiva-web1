package br.com.brumon.siscoletaseletiva.Controller;

import br.com.brumon.siscoletaseletiva.DTO.Request.AlterarSituacaoPontoRequestDTO;
import br.com.brumon.siscoletaseletiva.DTO.Request.PontoColetaRequestDTO;
import br.com.brumon.siscoletaseletiva.DTO.Response.PontoColetaResponseDTO;
import br.com.brumon.siscoletaseletiva.Service.PontoColetaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<PontoColetaResponseDTO>criarPontoColeta(@RequestBody @Valid PontoColetaRequestDTO dto) {
        PontoColetaResponseDTO pontoCriado = pontoColetaServ.salvarPontoColeta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pontoCriado);
    }

    //@RequestParam(required = false): permite que a requisição venha com ou sem filtros.
    @GetMapping
    public ResponseEntity<Page<PontoColetaResponseDTO>> listarPontoColetas(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String municipio,
            @RequestParam(required = false) String bairro,
            Pageable pageable) {

        //Verifica e chama o método correspondente no Service
        if (nome != null && !nome.isBlank()) {
            return ResponseEntity.ok(pontoColetaServ.findByNome(nome, pageable));
        } else if (municipio != null && !municipio.isBlank()) {
            return ResponseEntity.ok(pontoColetaServ.findByMunicipio(municipio, pageable));
        } else if (bairro != null && !bairro.isBlank()) {
            return ResponseEntity.ok(pontoColetaServ.findByBairro(bairro, pageable));
        }

        //Sem filtro, lista todos.
        return ResponseEntity.ok(pontoColetaServ.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PontoColetaResponseDTO> buscarPorId(@PathVariable Integer id) {
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
    public ResponseEntity<Void> apagarPontoColeta(@PathVariable Integer id) {
        pontoColetaServ.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
}


