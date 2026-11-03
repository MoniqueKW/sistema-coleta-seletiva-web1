package br.com.brumon.siscoletaseletiva.Service;

import br.com.brumon.siscoletaseletiva.DTO.Response.HistoricoResponseDTO;
import br.com.brumon.siscoletaseletiva.Entity.Historico;
import br.com.brumon.siscoletaseletiva.Entity.Solicitacao;
import br.com.brumon.siscoletaseletiva.Exception.RecursoNaoEncontradoException;
import br.com.brumon.siscoletaseletiva.Repository.HistoricoRepository;
import br.com.brumon.siscoletaseletiva.Repository.SolicitacaoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HistoricoService {
    private final HistoricoRepository rep;
    private final SolicitacaoRepository repSolicitacao;
    public HistoricoService(HistoricoRepository rep, SolicitacaoRepository repSolicitacao) {
        this.rep = rep;
        this.repSolicitacao = repSolicitacao;
    }
    public Page<HistoricoResponseDTO> findAll(Pageable pageable) {
        Page<Historico> historicos = rep.findAll(pageable);
        return historicos.map(historico -> new HistoricoResponseDTO(
                historico.getId(),
                historico.getSolicitacao().getId(),
                historico.getSituacaoAnterior(),
                historico.getSituacaoNova(),
                historico.getData(),
                historico.getResponsavel(),
                historico.getMotivo(),
                historico.getInfoTentativa()
        ));
    }

    public HistoricoResponseDTO findById(Integer id) {
        Historico historico = rep.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Histórico não encontrado"));
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

    public Page<HistoricoResponseDTO> findBySolicitacaoId(Integer idSolicitacao, Pageable pageable) {
        Page<Historico> historicos = rep.findBySolicitacao_IdOrderByDataAsc(idSolicitacao, pageable);
        return historicos.map(historico -> new HistoricoResponseDTO(
                historico.getId(),
                historico.getSolicitacao().getId(),
                historico.getSituacaoAnterior(),
                historico.getSituacaoNova(),
                historico.getData(),
                historico.getResponsavel(),
                historico.getMotivo(),
                historico.getInfoTentativa()
        ));
    }
    public void registrarHistorico(Integer idSolicitacao, String situacaoAnterior,
                                   String situacaoNova, String responsavel,
                                   String motivo, String infoTentativa) {
        Solicitacao solicitacao = repSolicitacao.findById(idSolicitacao)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Solicitação não encontrada"));
        Historico historico = Historico.builder()
                .solicitacao(solicitacao)
                .situacaoAnterior(situacaoAnterior)
                .situacaoNova(situacaoNova)
                .responsavel(responsavel)
                .motivo(motivo)
                .data(LocalDateTime.now())
                .infoTentativa(infoTentativa)
                .build();
        rep.save(historico);
    }
}