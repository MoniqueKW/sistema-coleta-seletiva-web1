package br.com.brumon.siscoletaseletiva.service;

import br.com.brumon.siscoletaseletiva.dto.request.OperadorRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.response.OperadorResponseDTO;
import br.com.brumon.siscoletaseletiva.model.entity.Operador;
import br.com.brumon.siscoletaseletiva.model.entity.PontoColeta;
import br.com.brumon.siscoletaseletiva.exception.RecursoDuplicadoException;
import br.com.brumon.siscoletaseletiva.exception.RecursoNaoEncontradoException;
import br.com.brumon.siscoletaseletiva.exception.RegraNegocioException;
import br.com.brumon.siscoletaseletiva.repository.HistoricoRepository;
import br.com.brumon.siscoletaseletiva.repository.OperadorRepository;
import br.com.brumon.siscoletaseletiva.repository.PontoColetaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OperadorService {
    private final OperadorRepository rep;
    private final PontoColetaRepository pontoRep;
    private final HistoricoRepository historicoRep;

    public OperadorService(OperadorRepository rep, PontoColetaRepository pontoRep, HistoricoRepository historicoRep) {
        this.rep = rep;
        this.pontoRep = pontoRep;
        this.historicoRep = historicoRep;
    }

    @Transactional(readOnly = true)
    public Page<OperadorResponseDTO> findAll(Pageable pageable) {
        Page<Operador> operadores = rep.findAll(pageable);
        return operadores.map(this::converterParaDTO);
    }

    @Transactional(readOnly = true)
    public OperadorResponseDTO findById(Integer id) {
        Operador operador = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Operador não " +
                "encontrado"));
        return converterParaDTO(operador);
    }

    @Transactional(readOnly = true)
    public Page<OperadorResponseDTO> findByPontoColetaId(Integer idPonto, Pageable pageable) {
        Page<Operador> operadores = rep.findByPontoColeta_Id(idPonto, pageable);
        return operadores.map(this::converterParaDTO);
    }

    @Transactional
    public OperadorResponseDTO salvarOperador(OperadorRequestDTO dto) {
        //deixa o email recebido em caixa baixa para evitar duplicidade de emails com letras maiusculas e minusculas
        String emailPadrao = dto.email().trim().toLowerCase();
        if (rep.existsByEmail(emailPadrao)) {
            throw new RecursoDuplicadoException("Já existe um operador cadastrado com este e-mail.");
        }
        PontoColeta ponto = pontoRep.findById(dto.idPonto())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ponto de coleta não encontrado"));
        Operador operador = rep.save(Operador.builder()
                .pontoColeta(ponto)
                .nome(dto.nome())
                .email(emailPadrao)
                .build());
        return converterParaDTO(operador);
    }

    @Transactional
    public OperadorResponseDTO atualizarOperador(Integer id, OperadorRequestDTO dto) {
        Operador operador = rep.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Operador não encontrado"));
        String emailPadrao = dto.email().trim().toLowerCase();
        if (rep.existsByEmail(emailPadrao) && !operador.getEmail().equals(emailPadrao)) {
            throw new RecursoDuplicadoException("Já existe um operador cadastrado com este e-mail.");
        }
        PontoColeta ponto = pontoRep.findById(dto.idPonto())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ponto de coleta não encontrado"));
        operador.setPontoColeta(ponto);
        operador.setNome(dto.nome());
        operador.setEmail(emailPadrao);
        Operador atualizado = rep.save(operador);
        return converterParaDTO(atualizado);
    }

    //Todo operador está sempre vinculado a um ponto de coleta como estabelecido na entidade.
    @Transactional
    public void deleteById(Integer id) {
        Operador operador = rep.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Operador não encontrado."
                ));

        if (historicoRep.existsByOperador_Id(id)) {
            throw new RegraNegocioException(
                    "Não é possível excluir o operador, pois ele possui registros vinculados no histórico de solicitações."
            );
        }
        rep.delete(operador);
    }

    public OperadorResponseDTO converterParaDTO(Operador operador) {
        return new OperadorResponseDTO(
                operador.getId(),
                operador.getPontoColeta().getId(),
                operador.getNome(),
                operador.getEmail()
        );
    }

}
