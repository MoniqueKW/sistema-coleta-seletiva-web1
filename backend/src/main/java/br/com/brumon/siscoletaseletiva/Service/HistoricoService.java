package br.com.brumon.siscoletaseletiva.Service;

import br.com.brumon.siscoletaseletiva.DTO.Response.HistoricoResponseDTO;
import br.com.brumon.siscoletaseletiva.Entity.Historico;
import br.com.brumon.siscoletaseletiva.Entity.Operador;
import br.com.brumon.siscoletaseletiva.Entity.Solicitacao;
import br.com.brumon.siscoletaseletiva.Exception.RecursoNaoEncontradoException;
import br.com.brumon.siscoletaseletiva.Exception.RegraNegocioException;
import br.com.brumon.siscoletaseletiva.Repository.HistoricoRepository;
import br.com.brumon.siscoletaseletiva.Repository.OperadorRepository;
import br.com.brumon.siscoletaseletiva.Repository.SolicitacaoRepository;
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
        return historicos.map(historico -> new HistoricoResponseDTO(
                historico.getId(),
                historico.getSolicitacao().getId(),
                historico.getSituacaoAnterior(),
                historico.getSituacaoNova(),
                historico.getData(),
                historico.getResponsavel().getNome(),
                historico.getMotivo(),
                historico.getInfoTentativa()
        ));
    }

    @Transactional(readOnly = true)
    public HistoricoResponseDTO findById(Integer id) {
        Historico historico = rep.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Histórico não encontrado"));
        return new HistoricoResponseDTO(
                historico.getId(),
                historico.getSolicitacao().getId(),
                historico.getSituacaoAnterior(),
                historico.getSituacaoNova(),
                historico.getData(),
                historico.getResponsavel().getNome(),
                historico.getMotivo(),
                historico.getInfoTentativa()
        );
    }
    @Transactional(readOnly = true)
    public Page<HistoricoResponseDTO> findByResponsavelId(Integer idOperador, Pageable pageable) {
        Page<Historico> historicos = rep.findByResponsavel_Id(idOperador, pageable);
        return historicos.map(historico -> new HistoricoResponseDTO(
                historico.getId(),
                historico.getSolicitacao().getId(),
                historico.getSituacaoAnterior(),
                historico.getSituacaoNova(),
                historico.getData(),
                historico.getResponsavel().getNome(),
                historico.getMotivo(),
                historico.getInfoTentativa()
        ));
    }
    @Transactional(readOnly = true)
    public Page<HistoricoResponseDTO> findBySolicitacaoId(Integer idSolicitacao, Pageable pageable) {
        Page<Historico> historicos = rep.findBySolicitacao_IdOrderByDataAsc(idSolicitacao, pageable);
        return historicos.map(historico -> new HistoricoResponseDTO(
                historico.getId(),
                historico.getSolicitacao().getId(),
                historico.getSituacaoAnterior(),
                historico.getSituacaoNova(),
                historico.getData(),
                historico.getResponsavel().getNome(),
                historico.getMotivo(),
                historico.getInfoTentativa()
        ));
    }
    public void registrarHistorico(Integer idSolicitacao, String situacaoAnterior,
                                   String situacaoNova, Integer idOperador,
                                   String motivo, String infoTentativa) {
        Solicitacao solicitacao = repSolicitacao.findById(idSolicitacao)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Solicitação não encontrada"));
        Operador operador = repOperador.findById(idOperador)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Operador não encontrado"));
        if (!operador.getPontoColeta().getId().equals(solicitacao.getPontoColeta().getId())) {
            throw new RegraNegocioException("Operador não pertence ao ponto de coleta desta solicitação");
        }
        Historico historico = Historico.builder()
                .solicitacao(solicitacao)
                .situacaoAnterior(situacaoAnterior)
                .situacaoNova(situacaoNova)
                .responsavel(operador)
                .motivo(motivo)
                .data(LocalDateTime.now())
                .infoTentativa(infoTentativa)
                .build();
        rep.save(historico);
    }
}