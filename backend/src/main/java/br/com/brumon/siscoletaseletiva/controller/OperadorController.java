package br.com.brumon.siscoletaseletiva.controller;

import br.com.brumon.siscoletaseletiva.dto.request.OperadorRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.response.OperadorResponseDTO;
import br.com.brumon.siscoletaseletiva.service.OperadorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/operadores")
public class OperadorController {

    private final OperadorService operadorServ;

    public OperadorController(OperadorService operadorServ) {
        this.operadorServ = operadorServ;
    }

    @PostMapping
    public ResponseEntity<OperadorResponseDTO> criarOperador(@RequestBody @Valid OperadorRequestDTO dto) {
        OperadorResponseDTO operadorCriado = operadorServ.salvarOperador(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(operadorCriado);
    }

    @GetMapping
    public ResponseEntity<Page<OperadorResponseDTO>> findAll(
            @RequestParam(required = false) Integer idPonto,
            Pageable pageable) {
        //Para filtrar por ponto de coleta
        if (idPonto != null) {
            return ResponseEntity.ok(operadorServ.findByPontoColetaId(idPonto, pageable));
        }
        //Para listar todos os operadores de forma paginada
        return ResponseEntity.ok(operadorServ.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperadorResponseDTO> findById(@PathVariable Integer id) {
        OperadorResponseDTO operador = operadorServ.findById(id);
        return ResponseEntity.ok(operador);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OperadorResponseDTO> atualizarOperador(
            @PathVariable Integer id,
            @RequestBody @Valid OperadorRequestDTO dto) {
        OperadorResponseDTO operadorAtualizado = operadorServ.atualizarOperador(id, dto);
        return ResponseEntity.ok(operadorAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        operadorServ.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}