package br.com.brumon.siscoletaseletiva.Service;

import br.com.brumon.siscoletaseletiva.DTO.Request.ItemSolicitacaoRequestDTO;
import br.com.brumon.siscoletaseletiva.DTO.Request.SolicitacaoRequestDTO;
import br.com.brumon.siscoletaseletiva.DTO.Response.ItemSolicitacaoResponseDTO;
import br.com.brumon.siscoletaseletiva.DTO.Response.SolicitacaoResponseDTO;
import br.com.brumon.siscoletaseletiva.Entity.*;
import br.com.brumon.siscoletaseletiva.Enum.SituacaoSolicitacao;
import br.com.brumon.siscoletaseletiva.Exception.RecursoNaoEncontradoException;
import br.com.brumon.siscoletaseletiva.Repository.*;
import jakarta.transaction.Transactional;
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
    private final MoradorRepository repMorador;
    private final PontoColetaRepository repPonto;
    private final MaterialRepository repMaterial;
    public SolicitacaoService(SolicitacaoRepository rep,
                              HistoricoRepository repHistorico,
                              ItemSolicitacaoRepository repItemSolicitacao,
                              HistoricoService serviceHistorico,
                              MoradorRepository repMorador,
                              PontoColetaRepository repPonto,
                              MaterialRepository repMaterial) {
        this.rep = rep;
        this.repHistorico = repHistorico;
        this.repItemSolicitacao = repItemSolicitacao;
        this.serviceHistorico = serviceHistorico;
        this.repMorador = repMorador;
        this.repPonto = repPonto;
        this.repMaterial = repMaterial;
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
    @Transactional
    public SolicitacaoResponseDTO salvarSolicitacao(SolicitacaoRequestDTO solicitacaodto) {
        Morador morador = repMorador.findById(solicitacaodto.idMorador())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Morador não encontrado"));
        PontoColeta ponto = repPonto.findById(solicitacaodto.idPonto())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ponto de coleta não encontrado"));
        Solicitacao solicitacao = rep.save(Solicitacao.builder()
                .morador(morador)
                .pontoColeta(ponto)
                .protocoloUnico(solicitacaodto.protocoloUnico())
                .copiaLogradouro(solicitacaodto.copiaLogradouro())
                .copiaNumero(solicitacaodto.copiaNumero())
                .copiaComplemento(solicitacaodto.copiaComplemento())
                .copiaBairro(solicitacaodto.copiaBairro())
                .copiaMunicipio(solicitacaodto.copiaMunicipio())
                .copiaUf(solicitacaodto.copiaUf())
                .copiaCep(solicitacaodto.copiaCep())
                .situacao(SituacaoSolicitacao.SOLICITADA)
                .dataCriacao(LocalDateTime.now())
                .observacoes(solicitacaodto.observacoes())
                //nao escrevemos .version() pq o hibernate vai controlar
                //esse campo para evitar concorrencias
                .build());
        // abaixo, fiz para preencher a tabela de ItemSolicitacao, é aqui que essa entidade
        // é implementada. Em cada nova solicitação, uma lista de itens de materiais é requerido,
        // dessa forma, essa lista é usada para preencher a tabela ItemSolicitação e fazer
        // a relação entre Solicitação e Material.
        for (ItemSolicitacaoRequestDTO itemDTO : solicitacaodto.itens()) {
            Material material = repMaterial.findById(itemDTO.idMaterial())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Material não encontrado."));
            ItemSolicitacao item = ItemSolicitacao.builder()
                    .solicitacao(solicitacao)
                    .material(material)
                    .copiaUnidade(material.getUnidade())
                    .qtdEstimada(itemDTO.qtdEstimada())
                    .build();
            repItemSolicitacao.save(item);
        }
        serviceHistorico.registrarHistorico(
                solicitacao.getId(),
                null,
                solicitacao.getSituacao().name(),
                "Sistema",
                "Solicitação criada",
                null);
        return converterParaDTO(solicitacao);
    }
    @Transactional
//    public SolicitacaoResponseDTO atualizarSolicitacao (Integer id, SolicitacaoRequestDTO solicitacaodto) {
//        if (!rep.existsById(id)) {
//            throw new RecursoNaoEncontradoException("Solicitação não encontrada");
//        }
//    }
    //nessa conversao para dto, pego uma lista de ItemSolicitacao procurando pelo seu repository,
    //pelo id da solicitação, e depois uso essa lista para retornar junto com os outros campos da
    //SolicitacaoResponseDTO.
    private SolicitacaoResponseDTO converterParaDTO(Solicitacao solicitacao) {
        List<ItemSolicitacaoResponseDTO> itens = solicitacao.getItens()
                .stream()
                .map(item -> new ItemSolicitacaoResponseDTO(
                        item.getMaterial().getId(),
                        item.getMaterial().getNome(),
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
                solicitacao.getCopiaBairro(),
                solicitacao.getCopiaMunicipio(),
                solicitacao.getCopiaUf(),
                solicitacao.getCopiaCep(),
                solicitacao.getCopiaComplemento(),
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
