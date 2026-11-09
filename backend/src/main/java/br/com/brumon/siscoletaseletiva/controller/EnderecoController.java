package br.com.brumon.siscoletaseletiva.controller;

import br.com.brumon.siscoletaseletiva.dto.request.EnderecoRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.request.EnderecoUpdateRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.response.EnderecoResponseDTO;
import br.com.brumon.siscoletaseletiva.service.EnderecoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @PostMapping
    public ResponseEntity<EnderecoResponseDTO> criarEndereco(@RequestBody @Valid EnderecoRequestDTO dto) {
        EnderecoResponseDTO enderecoCriado = enderecoService.salvarEndereco(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(enderecoCriado);
    }

    @GetMapping
    public ResponseEntity<Page<EnderecoResponseDTO>> listar(
            @RequestParam(required = false) Integer idMorador,
            @RequestParam(required = false) String cep,
            @RequestParam(required = false) String uf,
            @RequestParam(required = false) String municipio,
            @RequestParam(required = false) String bairro,
            @RequestParam(required = false) String logradouro,
            Pageable pageable) {
        // Se o usuário pediu 20, usa 20. Se pediu 500, limita a 100.
        Pageable limitacaoFixa = PageRequest.of(
                pageable.getPageNumber(),
                Math.min(pageable.getPageSize(), 100),
                pageable.getSort()
        );
        //Para retornar todos os endereços paginados
        return ResponseEntity.ok(enderecoService.listar(idMorador, cep, uf, municipio, bairro, logradouro, limitacaoFixa));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> findById(@PathVariable Integer id) {
        EnderecoResponseDTO endereco = enderecoService.findById(id);
        return ResponseEntity.ok(endereco);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> atualizarEndereco(
            @PathVariable Integer id,
            @RequestBody @Valid EnderecoUpdateRequestDTO dto) {
        EnderecoResponseDTO enderecoAtualizado = enderecoService.atualizarEndereco(id, dto);
        return ResponseEntity.ok(enderecoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        enderecoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}