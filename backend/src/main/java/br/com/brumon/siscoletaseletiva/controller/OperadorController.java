package br.com.brumon.siscoletaseletiva.controller;

import br.com.brumon.siscoletaseletiva.dto.request.OperadorRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.response.OperadorResponseDTO;
import br.com.brumon.siscoletaseletiva.service.OperadorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public ResponseEntity<Page<OperadorResponseDTO>> listar(
            @RequestParam(required = false) Integer idPonto,
            @RequestParam(required = false) String nome,
            Pageable pageable) {
        Pageable limitacaoFixa = PageRequest.of(
                pageable.getPageNumber(),
                Math.min(pageable.getPageSize(), 100),
                pageable.getSort()
        );
        //Para filtrar caso tenha o id pontocoleta, nesse caso simples dava para
        //fazer aqui mesmo, mas para padronizar melhor manter o metodo filtrar no service
        return ResponseEntity.ok(operadorServ.listar(idPonto, nome, limitacaoFixa));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperadorResponseDTO> findById(@PathVariable Integer id) {
        OperadorResponseDTO operador = operadorServ.findById(id);
        return ResponseEntity.ok(operador);
    }
    @GetMapping("/por-email")
    public ResponseEntity<OperadorResponseDTO> findByEmail(@RequestParam String email) {
        OperadorResponseDTO operador = operadorServ.findByEmail(email);
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