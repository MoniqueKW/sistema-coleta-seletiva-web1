package br.com.brumon.siscoletaseletiva.Service;

import br.com.brumon.siscoletaseletiva.DTO.Request.OperadorRequestDTO;
import br.com.brumon.siscoletaseletiva.DTO.Response.OperadorResponseDTO;
import br.com.brumon.siscoletaseletiva.Entity.Operador;
import br.com.brumon.siscoletaseletiva.Entity.PontoColeta;
import br.com.brumon.siscoletaseletiva.Enum.SituacaoPonto;
import br.com.brumon.siscoletaseletiva.Exception.RecursoDuplicadoException;
import br.com.brumon.siscoletaseletiva.Exception.RecursoNaoEncontradoException;
import br.com.brumon.siscoletaseletiva.Exception.RegraNegocioException;
import br.com.brumon.siscoletaseletiva.Repository.OperadorRepository;
import br.com.brumon.siscoletaseletiva.Repository.PontoColetaRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperadorService {
    private final OperadorRepository rep;
    private final PontoColetaRepository pontoRep;
    public OperadorService(OperadorRepository rep, PontoColetaRepository pontoRep) {
        this.rep = rep;
        this.pontoRep = pontoRep;
    }

    public Page<OperadorResponseDTO> findAll(Pageable pageable) {
        Page<Operador> operadores = rep.findAll(pageable);
        if (operadores.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum operador cadastrado");
        }
        return operadores
                .map(operador -> new OperadorResponseDTO(
                        operador.getId(),
                        operador.getPontoColeta().getId(),
                        operador.getNome(),
                        operador.getEmail()
                ));
    }

    public OperadorResponseDTO findById(Integer id) {
        Operador operador = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Operador não " +
                "encontrado"));
        return new OperadorResponseDTO(
                operador.getId(),
                operador.getPontoColeta().getId(),
                operador.getNome(),
                operador.getEmail()
        );
    }

    public Page<OperadorResponseDTO> findByPontoColetaId(Integer idPonto, Pageable pageable) {
        Page<Operador> operadores = rep.findByPontoColeta_Id(idPonto, pageable);
        if (operadores.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum operador encontrado para este ponto de coleta");
        }
        return operadores.map(operador -> new OperadorResponseDTO(
                operador.getId(),
                operador.getPontoColeta().getId(),
                operador.getNome(),
                operador.getEmail()
        ));
    }

    @Transactional
    public OperadorResponseDTO salvarOperador(OperadorRequestDTO dto) {
        if (rep.existsByEmail(dto.email())) {
            throw new RecursoDuplicadoException("Já existe um operador cadastrado com este e-mail.");
        }
        PontoColeta ponto = pontoRep.findById(dto.idPonto())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ponto de coleta não encontrado"));
        Operador operador = rep.save(Operador.builder()
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
        Operador operador = rep.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Operador não encontrado"));
        if (rep.existsByEmail(dto.email()) && !operador.getEmail().equals(dto.email())) {
            throw new RecursoDuplicadoException("Já existe um operador cadastrado com este e-mail.");
        }

        PontoColeta ponto = pontoRep.findById(dto.idPonto())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ponto de coleta não encontrado"));
        operador.setPontoColeta(ponto);
        operador.setNome(dto.nome());
        operador.setEmail(dto.email());
        Operador atualizado = rep.save(operador);
        return new OperadorResponseDTO(
                atualizado.getId(),
                atualizado.getPontoColeta().getId(),
                atualizado.getNome(),
                atualizado.getEmail()
        );
    }

    @Transactional
    public void deleteById(Integer id) {
        Operador operador = rep.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Operador não encontrado."
                ));
        //verificacao se ta vinculado a algum ponto
        if (operador.getPontoColeta() != null) {
            throw new RegraNegocioException(
                    "Não é possível excluir o operador enquanto ele estiver vinculado a um ponto de coleta."
            );
        }
        rep.delete(operador);
    }
}
