package br.com.brumon.siscoletaseletiva.service;

import br.com.brumon.siscoletaseletiva.dto.response.HistoricoResponseDTO;
import br.com.brumon.siscoletaseletiva.model.entity.Historico;
import br.com.brumon.siscoletaseletiva.model.entity.Operador;
import br.com.brumon.siscoletaseletiva.model.entity.Solicitacao;
import br.com.brumon.siscoletaseletiva.exception.RecursoNaoEncontradoException;
import br.com.brumon.siscoletaseletiva.exception.RegraNegocioException;
import br.com.brumon.siscoletaseletiva.repository.HistoricoRepository;
import br.com.brumon.siscoletaseletiva.repository.OperadorRepository;
import br.com.brumon.siscoletaseletiva.repository.SolicitacaoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class HistoricoService {
    private final HistoricoRepository rep;
    private final SolicitacaoRepository repSolicitacao;
    private final OperadorRepository repOperador;
    public HistoricoService(HistoricoRepository rep, SolicitacaoRepository repSolicitacao,
                            OperadorRepository repOperador) {
        this.rep = rep;
        this.repSolicitacao = repSolicitacao;
        this.repOperador = repOperador;
    }
    @Transactional(readOnly = true)
    public Page<HistoricoResponseDTO> findAll(Pageable pageable) {
        Page<Historico> historicos = rep.findAll(pageable);
        return historicos.map(this::converterParaDTO);
    }

    @Transactional(readOnly = true)
    public HistoricoResponseDTO findById(Integer id) {
        Historico historico = rep.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Histórico não encontrado"));
        return converterParaDTO(historico);
    }
    @Transactional(readOnly = true)
    public Page<HistoricoResponseDTO> findByOperadorId(Integer idOperador, Pageable pageable) {
        Page<Historico> historicos = rep.findByOperador_Id(idOperador, pageable);
        return historicos.map(this::converterParaDTO);
    }
    @Transactional(readOnly = true)
    public Page<HistoricoResponseDTO> findBySolicitacaoId(Integer idSolicitacao, Pageable pageable) {
        Page<Historico> historicos = rep.findBySolicitacao_IdOrderByDataAsc(idSolicitacao, pageable);
        return historicos.map(this::converterParaDTO);
    }
    @Transactional(readOnly = true)
    public Page<HistoricoResponseDTO> findBySolicitacaoIdAndOperadorId(
            Integer idSolicitacao, Integer idOperador, Pageable pageable) {
        Page<Historico> historicos = rep.findBySolicitacao_IdAndOperador_IdOrderByDataAsc(
                idSolicitacao, idOperador, pageable);
        return historicos.map(this::converterParaDTO);
    }
    @Transactional(readOnly = true)
    public Page<HistoricoResponseDTO> findBySolicitacaoIdAndResponsavel(
            Integer idSolicitacao, String nomeResponsavel, Pageable pageable) {
        Page<Historico> historicos = rep.findBySolicitacao_IdAndResponsavel(
                idSolicitacao, nomeResponsavel, pageable);
        return historicos.map(this::converterParaDTO);
    }

    public void registrarHistorico(Integer idSolicitacao, String situacaoAnterior,
                                   String situacaoNova, Integer idOperador,
                                   String motivo, String infoTentativa) {
        Solicitacao solicitacao = repSolicitacao.findById(idSolicitacao)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Solicitação não encontrada"));
        Operador operador = null;
        String nomeResponsavel;
        if (idOperador != null) {
            operador = repOperador.findById(idOperador).orElseThrow(() -> new RecursoNaoEncontradoException("Operador não encontrado"));
            nomeResponsavel = operador.getNome();
        } else {
            nomeResponsavel = solicitacao.getMorador().getNome();
        }
        Historico historico = Historico.builder()
                .solicitacao(solicitacao)
                .situacaoAnterior(situacaoAnterior)
                .situacaoNova(situacaoNova)
                .responsavel(nomeResponsavel)
                .motivo(motivo)
                .data(LocalDateTime.now())
                .infoTentativa(infoTentativa)
                .operador(operador)
                .build();
        rep.save(historico);
    }

    public HistoricoResponseDTO converterParaDTO(Historico historico) {
        return new HistoricoResponseDTO(
                historico.getId(),
                historico.getSolicitacao().getId(),
                historico.getSituacaoAnterior(),
                historico.getSituacaoNova(),
                historico.getData(),
                historico.getResponsavel(),
                historico.getMotivo(),
                historico.getInfoTentativa()
        );
    }
}