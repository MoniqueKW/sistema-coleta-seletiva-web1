package br.com.brumon.siscoletaseletiva.Controller;

import br.com.brumon.siscoletaseletiva.DTO.Request.MoradorRequestDTO;
import br.com.brumon.siscoletaseletiva.DTO.Response.MoradorResponseDTO;
import br.com.brumon.siscoletaseletiva.Service.MoradorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<Page<MoradorResponseDTO>> listarMoradores(
            //o sistema não vai dar erro se a pessoa não enviar nenhum nome.
            @RequestParam(required = false) String nome,
            Pageable pageable) {
        if (nome != null && !nome.isBlank()) {
            return ResponseEntity.ok(moradorServ.findByNome(nome, pageable));
        }
        return ResponseEntity.ok(moradorServ.findAll(pageable));
        //retorna uma lista paginada com todos os moradores cadastrados.
    }

    @GetMapping("/{id}")
    public ResponseEntity<MoradorResponseDTO> buscarPorId(@PathVariable Integer id) {
        MoradorResponseDTO morador = moradorServ.findById(id);
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
    public ResponseEntity<Void> apagarMorador(@PathVariable Integer id) {
        moradorServ.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}