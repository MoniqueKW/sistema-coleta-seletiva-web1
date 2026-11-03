package br.com.brumon.siscoletaseletiva.Service;

import br.com.brumon.siscoletaseletiva.DTO.Response.ItemSolicitacaoResponseDTO;
import br.com.brumon.siscoletaseletiva.DTO.Response.SolicitacaoResponseDTO;
import br.com.brumon.siscoletaseletiva.Entity.Solicitacao;
import br.com.brumon.siscoletaseletiva.Enum.SituacaoSolicitacao;
import br.com.brumon.siscoletaseletiva.Exception.RecursoNaoEncontradoException;
import br.com.brumon.siscoletaseletiva.Repository.HistoricoRepository;
import br.com.brumon.siscoletaseletiva.Repository.ItemSolicitacaoRepository;
import br.com.brumon.siscoletaseletiva.Repository.SolicitacaoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SolicitacaoService {
    private final SolicitacaoRepository rep;
    private final HistoricoRepository repHistorico;
    private final ItemSolicitacaoRepository repItemSolicitacao;
    private final HistoricoService serviceHistorico;
    public SolicitacaoService(SolicitacaoRepository rep,
                              HistoricoRepository repHistorico,
                              ItemSolicitacaoRepository repItemSolicitacao,
                              HistoricoService serviceHistorico) {
        this.rep = rep;
        this.repHistorico = repHistorico;
        this.repItemSolicitacao = repItemSolicitacao;
        this.serviceHistorico = serviceHistorico;
    }
    public Page<SolicitacaoResponseDTO> findAll(Pageable pageable) {
        Page<Solicitacao> solicitacoes = rep.findAll(pageable);
        return solicitacoes.map(solicitacao -> converterParaDTO(solicitacao));
    }
    public SolicitacaoResponseDTO findById(Integer id) {
        Solicitacao solicitacao = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Solicitação não encontrada"));
        return converterParaDTO(solicitacao);
    }
    public Page<SolicitacaoResponseDTO> findBySituacao(SituacaoSolicitacao situacao, Pageable pageable) {
        Page<Solicitacao> solicitacoes = rep.findBySituacao(situacao, pageable);
        return solicitacoes.map(solicitacao -> converterParaDTO(solicitacao));
    }
    public Page<SolicitacaoResponseDTO> findByPontoColetaIdAndMoradorId(Integer idPonto, Integer idMorador, Pageable pageable) {
        Page<Solicitacao> solicitacoes = rep.findByPontoColeta_IdAndMorador_Id(idPonto, idMorador, pageable);
        return solicitacoes.map(solicitacao -> converterParaDTO(solicitacao));
    }
    public Page<SolicitacaoResponseDTO> findByPontoColetaId(Integer idPonto, Pageable pageable) {
        Page<Solicitacao> solicitacoes = rep.findByPontoColeta_Id(idPonto, pageable);
        return solicitacoes.map(solicitacao -> converterParaDTO(solicitacao));
    }
    public Page<SolicitacaoResponseDTO> findByPontoColetaIdAndSituacaoAndDataCriacaoBetween(Integer idPonto, SituacaoSolicitacao situacao,
                                                                                            LocalDateTime dataInicio,
                                                                                            LocalDateTime dataFim,
                                                                                            Pageable pageable) {
        Page<Solicitacao> solicitacoes = rep.findByPontoColeta_IdAndSituacaoAndDataCriacaoBetween(idPonto,
                situacao,
                dataInicio, dataFim, pageable);
        return solicitacoes.map(solicitacao -> converterParaDTO(solicitacao));
    }
    public Page<SolicitacaoResponseDTO> findByPontoColetaIdAndDataCriacaoBetween(Integer idPonto, LocalDateTime dataInicio,
                                                                                 LocalDateTime dataFim, Pageable pageable) {
        Page<Solicitacao> solicitacoes = rep.findByPontoColeta_IdAndDataCriacaoBetween(idPonto, dataInicio, dataFim, pageable);
        return solicitacoes.map(solicitacao -> converterParaDTO(solicitacao));
    }
    public Page<SolicitacaoResponseDTO> findByPontoColetaIdAndSituacao(Integer idPonto, SituacaoSolicitacao situacao, Pageable pageable) {
        Page<Solicitacao> solicitacoes = rep.findByPontoColeta_IdAndSituacao(idPonto, situacao, pageable);
        return solicitacoes.map(solicitacao -> converterParaDTO(solicitacao));
    }
    public Page<SolicitacaoResponseDTO> findByMoradorId(Integer idMorador, Pageable pageable) {
        Page<Solicitacao> solicitacoes = rep.findByMorador_Id(idMorador, pageable);
        return solicitacoes.map(solicitacao -> converterParaDTO(solicitacao));
    }
    public Page<SolicitacaoResponseDTO> findByMoradorIdAndSituacao(Integer idMorador, SituacaoSolicitacao situacao, Pageable pageable) {
        Page<Solicitacao> solicitacoes = rep.findByMorador_IdAndSituacao(idMorador, situacao, pageable);
        return solicitacoes.map(solicitacao -> converterParaDTO(solicitacao));
    }
    public Page<SolicitacaoResponseDTO> findByMoradorIdAndSituacaoAndDataCriacaoBetween(Integer idMorador, SituacaoSolicitacao situacao,
                                                                                   LocalDateTime dataInicio, LocalDateTime dataFim, Pageable pageable) {
        Page<Solicitacao> solicitacoes = rep.findByMorador_IdAndSituacaoAndDataCriacaoBetween(idMorador, situacao, dataInicio, dataFim, pageable);
        return solicitacoes.map(solicitacao -> converterParaDTO(solicitacao));
    }
    public Page<SolicitacaoResponseDTO> findByMoradorIdAndDataCriacaoBetween(Integer idMorador, LocalDateTime dataInicio,
                                                                             LocalDateTime dataFim, Pageable pageable) {
        Page<Solicitacao> solicitacoes = rep.findByMorador_IdAndDataCriacaoBetween(idMorador, dataInicio, dataFim, pageable);
        return solicitacoes.map(solicitacao -> converterParaDTO(solicitacao));
    }
    public SolicitacaoResponseDTO converterParaDTO(Solicitacao solicitacao) {
        List<ItemSolicitacaoResponseDTO> itens = repItemSolicitacao.findBySolicitacao_Id(solicitacao.getId())
                .stream()
                .map(item -> new ItemSolicitacaoResponseDTO(
                        item.getSolicitacao().getId(),
                        item.getMaterial().getId(),
                        item.getCopiaUnidade(),
                        item.getQtdEstimada(),
                        item.getQtdEfetiva()
                ))
                .toList();
        return new SolicitacaoResponseDTO(
                solicitacao.getId(),
                solicitacao.getMorador().getId(),
                solicitacao.getPontoColeta().getId(),
                solicitacao.getProtocoloUnico(),
                solicitacao.getCopiaLogradouro(),
                solicitacao.getCopiaNumero(),
                solicitacao.getCopiaComplemento(),
                solicitacao.getCopiaBairro(),
                solicitacao.getCopiaMunicipio(),
                solicitacao.getCopiaUf(),
                solicitacao.getCopiaCep(),
                solicitacao.getSituacao(),
                solicitacao.getDataCriacao(),
                solicitacao.getDataAgendamento(),
                solicitacao.getDataConclusao(),
                solicitacao.getObservacoes(),
                solicitacao.getVersao(),
                itens
        );
    }
}
