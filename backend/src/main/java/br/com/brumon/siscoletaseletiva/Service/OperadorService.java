package br.com.brumon.siscoletaseletiva.Service;

import br.com.brumon.siscoletaseletiva.DTO.Request.OperadorRequestDTO;
import br.com.brumon.siscoletaseletiva.DTO.Response.OperadorResponseDTO;
import br.com.brumon.siscoletaseletiva.Entity.Operador;
import br.com.brumon.siscoletaseletiva.Entity.PontoColeta;
import br.com.brumon.siscoletaseletiva.Exception.RecursoDuplicadoException;
import br.com.brumon.siscoletaseletiva.Exception.RecursoNaoEncontradoException;
import br.com.brumon.siscoletaseletiva.Repository.OperadorRepository;
import br.com.brumon.siscoletaseletiva.Repository.PontoColetaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperadorService {

    private final OperadorRepository opRep;
    private final PontoColetaRepository pontoRep;

    public OperadorService(OperadorRepository opRep, PontoColetaRepository pontoRep) {
        this.opRep = opRep;
        this.pontoRep = pontoRep;
    }

    public List<OperadorResponseDTO> findAll() {
        List<Operador> lista = opRep.findAll();
        if (lista.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum operador cadastrado");
        }
        return lista.stream()
                .map(operador -> new OperadorResponseDTO(
                        operador.getId(),
                        operador.getPontoColeta().getId(),
                        operador.getNome(),
                        operador.getEmail()
                ))
                .toList();
    }

    public OperadorResponseDTO findById(Integer id) {
        Operador operador = opRep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Operador não encontrado"));
        return new OperadorResponseDTO(
                operador.getId(),
                operador.getPontoColeta().getId(),
                operador.getNome(),
                operador.getEmail()
        );
    }

    public List<OperadorResponseDTO> findByPontoColeta(Integer idPonto) {
        List<Operador> lista = opRep.findByPontoColeta_Id(idPonto);
        if (lista.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum operador encontrado para este ponto de coleta");
        }
        return lista.stream()
                .map(operador -> new OperadorResponseDTO(
                        operador.getId(),
                        operador.getPontoColeta().getId(),
                        operador.getNome(),
                        operador.getEmail()
                ))
                .toList();
    }

    @Transactional
    public OperadorResponseDTO salvarOperador(OperadorRequestDTO dto) {
        //Verificar duplicação de email
        if (opRep.findByEmail(dto.email()).isPresent()) {
            throw new RecursoDuplicadoException("Já existe um operador cadastrado com este e-mail.");
        }

        PontoColeta ponto = pontoRep.findById(dto.idPonto())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ponto de coleta não encontrado"));

        Operador operador = opRep.save(Operador.builder()
                .pontoColeta(ponto)
                .nome(dto.nome())
                .email(dto.email())
                .build());

        return new OperadorResponseDTO(
                operador.getId(),
                operador.getPontoColeta().getId(),
                operador.getNome(),
                operador.getEmail()
        );
    }

    @Transactional
    public OperadorResponseDTO atualizarOperador(Integer id, OperadorRequestDTO dto) {
        Operador operador = opRep.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Operador não encontrado"));

        //Verificação de email (evitar duplicação)
        opRep.findByEmail(dto.email()).ifPresent(existente -> {
            if (!existente.getId().equals(id)) {
                throw new RecursoDuplicadoException("Este e-mail já está vinculado a outro operador.");
            }
        });

        PontoColeta ponto = pontoRep.findById(dto.idPonto())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ponto de coleta não encontrado"));

        operador.setPontoColeta(ponto);
        operador.setNome(dto.nome());
        operador.setEmail(dto.email());

        Operador atualizado = opRep.save(operador);

        return new OperadorResponseDTO(
                atualizado.getId(),
                atualizado.getPontoColeta().getId(),
                atualizado.getNome(),
                atualizado.getEmail()
        );
    }

    public void deleteById(Integer id) {
        if (opRep.existsById(id)) {
            opRep.deleteById(id);
        } else {
            throw new RecursoNaoEncontradoException("Operador não encontrado");
        }
    }
}
