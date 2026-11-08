package br.com.brumon.siscoletaseletiva.controller;

import br.com.brumon.siscoletaseletiva.dto.response.HistoricoResponseDTO;
import br.com.brumon.siscoletaseletiva.service.HistoricoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/historicos")
public class HistoricoController {
    private final HistoricoService historicoServ;

    public HistoricoController(HistoricoService historicoServ) {
        this.historicoServ = historicoServ;
    }

    @GetMapping
    public ResponseEntity<Page<HistoricoResponseDTO>> findAll(
            @RequestParam(required = false) Integer idOperador,
            @RequestParam(required = false) Integer idSolicitacao,
            Pageable pageable) {
        Pageable limitacaoFixa = PageRequest.of(
                pageable.getPageNumber(),
                Math.min(pageable.getPageSize(), 100),
                pageable.getSort()
        );
        return ResponseEntity.ok(historicoServ.filtrar(idOperador, idSolicitacao, limitacaoFixa));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoricoResponseDTO> findById(@PathVariable Integer id) {
        HistoricoResponseDTO historico = historicoServ.findById(id);
        return ResponseEntity.ok(historico);
    }
}
