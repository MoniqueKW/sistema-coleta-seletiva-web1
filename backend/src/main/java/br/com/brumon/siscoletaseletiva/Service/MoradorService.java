package br.com.brumon.siscoletaseletiva.Service;

import br.com.brumon.siscoletaseletiva.DTO.Request.MoradorRequestDTO;
import br.com.brumon.siscoletaseletiva.DTO.Response.MoradorResponseDTO;
import br.com.brumon.siscoletaseletiva.Entity.Morador;
import br.com.brumon.siscoletaseletiva.Exception.RecursoNaoEncontradoException;
import br.com.brumon.siscoletaseletiva.Repository.MoradorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoradorService {
    private final MoradorRepository rep;
    public MoradorService(MoradorRepository rep) {
        this.rep = rep;
    }

    public List<MoradorResponseDTO> findAll() {
        List<Morador> lista = rep.findAll();
        if (lista.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum morador cadastrado");
        }
        return lista.stream()
                .map(morador -> new MoradorResponseDTO(
                    morador.getId(),
                    morador.getNome(),
                    morador.getEmail(),
                    morador.getTelefone()
                ))
                .toList();
    }

    public MoradorResponseDTO findById(Integer id) {
        Morador morador = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Morador não encontrado"));
        return new MoradorResponseDTO(
                morador.getId(),
                morador.getNome(),
                morador.getEmail(),
                morador.getTelefone()
        );
    }

    public List<MoradorResponseDTO> findByNome(String nome) {
        List<Morador> lista = rep.findByNomeContainingIgnoreCase(nome);
        if (lista.isEmpty()) {
            throw new RecursoNaoEncontradoException("Morador não encontrado");
        }
        return lista.stream()
                .map(morador -> new MoradorResponseDTO(
                        morador.getId(),
                        morador.getNome(),
                        morador.getEmail(),
                        morador.getTelefone()
                ))
                .toList();
    }
    public MoradorResponseDTO findByEmail(String email) {
        Morador morador = rep.findByEmail(email).orElseThrow(() -> new RecursoNaoEncontradoException("Morador não encontrado"));
        return new MoradorResponseDTO(
                morador.getId(),
                morador.getNome(),
                morador.getEmail(),
                morador.getTelefone()
        );
    }
    public MoradorResponseDTO findByTelefone(String telefone) {
        Morador morador = rep.findByTelefone(telefone).orElseThrow(() -> new RecursoNaoEncontradoException("Morador não encontrado"));
        return new MoradorResponseDTO(
                morador.getId(),
                morador.getNome(),
                morador.getEmail(),
                morador.getTelefone()
        );
    }
    public MoradorResponseDTO salvarMorador(MoradorRequestDTO morador) {
        Morador morador1 = rep.save(Morador.builder()
                .nome(morador.nome())
                .email(morador.email())
                .telefone(morador.telefone())
                .build());
        return new MoradorResponseDTO(
                morador1.getId(),
                morador1.getNome(),
                morador1.getEmail(),
                morador1.getTelefone()
        );
    }
//
    public MoradorResponseDTO atualizarMorador(MoradorRequestDTO morador){
        //continuar
        return null;
    }
}
