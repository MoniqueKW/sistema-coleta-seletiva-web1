package br.com.brumon.siscoletaseletiva.Service;

import br.com.brumon.siscoletaseletiva.DTO.Request.ItemSolicitacaoRequestDTO;
import br.com.brumon.siscoletaseletiva.DTO.Request.SolicitacaoRequestDTO;
import br.com.brumon.siscoletaseletiva.DTO.Response.ItemSolicitacaoResponseDTO;
import br.com.brumon.siscoletaseletiva.DTO.Response.SolicitacaoResponseDTO;
import br.com.brumon.siscoletaseletiva.Entity.*;
import br.com.brumon.siscoletaseletiva.Enum.SituacaoMaterial;
import br.com.brumon.siscoletaseletiva.Enum.SituacaoPonto;
import br.com.brumon.siscoletaseletiva.Enum.SituacaoSolicitacao;
import br.com.brumon.siscoletaseletiva.Exception.RecursoNaoEncontradoException;
import br.com.brumon.siscoletaseletiva.Exception.RegraNegocioException;
import br.com.brumon.siscoletaseletiva.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class SolicitacaoService {
    private final SolicitacaoRepository rep;
    private final HistoricoRepository repHistorico;
    private final ItemSolicitacaoRepository repItemSolicitacao;
    private final HistoricoService serviceHistorico;
    private final MoradorRepository repMorador;
    private final PontoColetaRepository repPonto;
    private final MaterialRepository repMaterial;
    private final MaterialAceitoRepository repMaterialAceito;
    public SolicitacaoService(SolicitacaoRepository rep,
                              HistoricoRepository repHistorico,
                              ItemSolicitacaoRepository repItemSolicitacao,
                              HistoricoService serviceHistorico,
                              MoradorRepository repMorador,
                              PontoColetaRepository repPonto,
                              MaterialRepository repMaterial,
                              MaterialAceitoRepository repMaterialAceito) {
        this.rep = rep;
        this.repHistorico = repHistorico;
        this.repItemSolicitacao = repItemSolicitacao;
        this.serviceHistorico = serviceHistorico;
        this.repMorador = repMorador;
        this.repPonto = repPonto;
        this.repMaterial = repMaterial;
        this.repMaterialAceito = repMaterialAceito;
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
        if (ponto.getSituacao() != SituacaoPonto.ATIVO) {
            throw new RegraNegocioException("O ponto de coleta não está ativo.");
        }
        //verifica se o ponto permite recolhimento domiciliar, se for o caso
        if (solicitacaodto.recolhimentoDomiciliar() && !ponto.getRecolhimentoDomiciliar()) {
            throw new RegraNegocioException("Este ponto de coleta não oferece recolhimento domiciliar.");
        }
        if (solicitacaodto.itens() == null || solicitacaodto.itens().isEmpty()) {
            throw new RegraNegocioException("A solicitação deve conter pelo menos um item.");
        }
        // abaixo, fiz isso tudo para preencher a tabela de ItemSolicitacao, é aqui que essa entidade
        // é implementada. Em cada nova solicitação, uma lista de itens de materiais é requerido,
        // dessa forma, essa lista é usada aqui para preencher a tabela ItemSolicitação e, dessa forma,
        // fazer a relação entre Solicitação e Material. Além disso, adiciono os itens no próprio
        // campo de itens da entidade Solicitação, só para guardar os dados.
        List<ItemSolicitacao> itensValidados = new ArrayList<>();
        Set<Integer> materiaisAdicionados = new HashSet<>();
        for (ItemSolicitacaoRequestDTO itemDTO : solicitacaodto.itens()) {
            Material material = repMaterial.findById(itemDTO.idMaterial())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Material não encontrado."));
            //verifica se o material está ativo
            if (material.getSituacao() != SituacaoMaterial.ATIVO) {
                throw new RegraNegocioException("O material não está ativo e não pode ser solicitado.");
            }
            // Usando o set lá de cima, o sistema verifica se o material é repetido. Set não aceita itens
            // duplicados, então fazer uma condição num add dele se encaixa perfeitamente
            if (!materiaisAdicionados.add(material.getId())) {
                throw new RegraNegocioException("O mesmo material não pode ser adicionado duas vezes à solicitação.");
            }
            //verifica se a qtdEstimada do material é maior que zero
            if (itemDTO.qtdEstimada() == null || itemDTO.qtdEstimada().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RegraNegocioException("A quantidade estimada do material deve ser maior que zero.");
            }
            //verifica o limite de recebimento, comparando se a qtdEstimada é maior que limiteOpcional
            MaterialAceitoId id = new MaterialAceitoId(ponto.getId(), material.getId());
            MaterialAceito materialAceito = repMaterialAceito.findById(id)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("O material não está associado ao ponto de coleta."));
            if(materialAceito.getLimiteOpcional() != null && itemDTO.qtdEstimada().compareTo(materialAceito.getLimiteOpcional()) > 0) {
                throw new RegraNegocioException("A quantidade solicitada ultrapassa o limite aceito para este material.");
            }
            ItemSolicitacao item = ItemSolicitacao.builder()
                    //ainda n seto o atributo solicitacao pq a variavel dele
                    // ainda n foi criada nesse ponto, mas dps irei setar num
                    // for each
                    .material(material)
                    .copiaUnidade(material.getUnidade())
                    .qtdEstimada(itemDTO.qtdEstimada())
                    .build();
            itensValidados.add(item);
        }
        Solicitacao solicitacao = rep.save(Solicitacao.builder()
                .morador(morador)
                .pontoColeta(ponto)
                .protocoloUnico(gerarProtocolo())
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
                .recolhimentoDomiciliar(solicitacaodto.recolhimentoDomiciliar())
                //nao escrevemos .version() pq o hibernate vai controlar
                //esse campo para evitar concorrencias
                .build());
        for (ItemSolicitacao item : itensValidados) {
           //agora, seto o atributo solicitacao de ItemSolicitacao que tinha faltado
            item.setSolicitacao(solicitacao);
            solicitacao.getItens().add(item);
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
    //nessa conversao para dto, pego uma lista de ItemSolicitacao atribuindo a ela,
    //o campo dos itens da solicitacao, e depois uso essa lista para retornar junto com os outros campos da
    //SolicitacaoResponseDTO, todos agora transformados em ItemSolicitacaoResponseDTO.
    private SolicitacaoResponseDTO converterParaDTO(Solicitacao solicitacao) {
        List<ItemSolicitacaoResponseDTO> itens = solicitacao.getItens()
                .stream()
                .map(item -> new ItemSolicitacaoResponseDTO(
                        item.getSolicitacao().getId(),
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
                solicitacao.getRecolhimentoDomiciliar(),
                solicitacao.getVersao(),
                itens
        );
    }
    // Fiz esse metodo para gerar um protocolo unico para cada
    // solicitação, usando UUID, passivel a alteracoes
    private String gerarProtocolo() {
        String protocolo;
        do {
            protocolo = "SC-" + UUID.randomUUID()
                    .toString()
                    .substring(0, 8)
                    .toUpperCase();
        } while (rep.existsByProtocoloUnico(protocolo));
        return protocolo;
    }
}
