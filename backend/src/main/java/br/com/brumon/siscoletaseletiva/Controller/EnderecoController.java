package br.com.brumon.siscoletaseletiva.Controller;

import br.com.brumon.siscoletaseletiva.DTO.Request.EnderecoRequestDTO;
import br.com.brumon.siscoletaseletiva.DTO.Response.EnderecoResponseDTO;
import br.com.brumon.siscoletaseletiva.Service.EnderecoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<Page<EnderecoResponseDTO>> listarEnderecos(
            @RequestParam(required = false) Integer idMorador,
            @RequestParam(required = false) String cep,
            @RequestParam(required = false) String uf,
            @RequestParam(required = false) String municipio,
            @RequestParam(required = false) String bairro,
            @RequestParam(required = false) String logradouro,
            Pageable pageable) {

        //Verifica os filtros passados na URL na ordem de maior prioridade
        if (idMorador != null) {
            return ResponseEntity.ok(enderecoService.findByMoradorId(idMorador, pageable));
        } else if (cep != null && !cep.isBlank()) {
            return ResponseEntity.ok(enderecoService.findByCep(cep, pageable));
        } else if (logradouro != null && !logradouro.isBlank()) {
            return ResponseEntity.ok(enderecoService.findByLogradouro(logradouro, pageable));
        } else if (bairro != null && !bairro.isBlank()) {
            return ResponseEntity.ok(enderecoService.findByBairro(bairro, pageable));
        } else if (municipio != null && !municipio.isBlank()) {
            return ResponseEntity.ok(enderecoService.findByMunicipio(municipio, pageable));
        } else if (uf != null && !uf.isBlank()) {
            return ResponseEntity.ok(enderecoService.findByUf(uf, pageable));
        }

        //Para retornar todos os endereços paginados
        return ResponseEntity.ok(enderecoService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> buscarPorId(@PathVariable Integer id) {
        EnderecoResponseDTO endereco = enderecoService.findById(id);
        return ResponseEntity.ok(endereco);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> atualizarEndereco(
            @PathVariable Integer id,
            @RequestBody @Valid EnderecoRequestDTO dto) {
        EnderecoResponseDTO enderecoAtualizado = enderecoService.atualizarEndereco(id, dto);
        return ResponseEntity.ok(enderecoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagarEndereco(@PathVariable Integer id) {
        enderecoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}