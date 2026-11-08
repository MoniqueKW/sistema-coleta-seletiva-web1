package br.com.brumon.siscoletaseletiva.controller;

import br.com.brumon.siscoletaseletiva.dto.request.MoradorRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.response.MoradorResponseDTO;
import br.com.brumon.siscoletaseletiva.service.MoradorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/moradores")
public class MoradorController {
    private final MoradorService moradorServ;
    public MoradorController(MoradorService moradorServ) {
        this.moradorServ = moradorServ;
    }

    @PostMapping
    public ResponseEntity<MoradorResponseDTO> criarMorador(@RequestBody @Valid MoradorRequestDTO dto) {
        MoradorResponseDTO moradorSalvo = moradorServ.salvarMorador(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(moradorSalvo);
    }

    @GetMapping
    public ResponseEntity<Page<MoradorResponseDTO>> findAll(
            //o sistema não vai dar erro se a pessoa não enviar nenhum nome.
            @RequestParam(required = false) String nome,
            Pageable pageable) {
        Pageable limitacaoFixa = PageRequest.of(
                pageable.getPageNumber(),
                Math.min(pageable.getPageSize(), 100),
                pageable.getSort()
        );
        return ResponseEntity.ok(moradorServ.filtrar(nome, limitacaoFixa));
        //retorna uma lista paginada com todos os moradores cadastrados.
    }

    @GetMapping("/{id}")
    public ResponseEntity<MoradorResponseDTO> findById(@PathVariable Integer id) {
        MoradorResponseDTO morador = moradorServ.findById(id);
        return ResponseEntity.ok(morador);
    }

    @GetMapping("/por-email")
    public ResponseEntity<MoradorResponseDTO> findByEmail(@RequestParam String email) {
        MoradorResponseDTO morador = moradorServ.findByEmail(email);
        return ResponseEntity.ok(morador);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MoradorResponseDTO> atualizarMorador(
            @PathVariable Integer id,
            @RequestBody @Valid MoradorRequestDTO dto) {
        MoradorResponseDTO moradorAtualizado = moradorServ.atualizarMorador(dto, id);
        return ResponseEntity.ok(moradorAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        moradorServ.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}