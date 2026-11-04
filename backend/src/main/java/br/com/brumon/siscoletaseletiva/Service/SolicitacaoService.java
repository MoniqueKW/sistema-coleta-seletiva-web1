package br.com.brumon.siscoletaseletiva.Service;

import br.com.brumon.siscoletaseletiva.DTO.Request.*;
import br.com.brumon.siscoletaseletiva.DTO.Response.ItemSolicitacaoResponseDTO;
import br.com.brumon.siscoletaseletiva.DTO.Response.SolicitacaoResponseDTO;
import br.com.brumon.siscoletaseletiva.Entity.*;
import br.com.brumon.siscoletaseletiva.Enum.SituacaoMaterial;
import br.com.brumon.siscoletaseletiva.Enum.SituacaoPonto;
import br.com.brumon.siscoletaseletiva.Enum.SituacaoSolicitacao;
import br.com.brumon.siscoletaseletiva.Exception.RecursoNaoEncontradoException;
import br.com.brumon.siscoletaseletiva.Exception.RegraNegocioException;
import br.com.brumon.siscoletaseletiva.Repository.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class SolicitacaoService {
    private final SolicitacaoRepository rep;
    private final ItemSolicitacaoRepository repItemSolicitacao;
    private final HistoricoService serviceHistorico;
    private final MoradorRepository repMorador;
    private final PontoColetaRepository repPonto;
    private final MaterialRepository repMaterial;
    private final MaterialAceitoRepository repMaterialAceito;
    private final OperadorRepository repOperador;
    public SolicitacaoService(SolicitacaoRepository rep,
                              ItemSolicitacaoRepository repItemSolicitacao,
                              HistoricoService serviceHistorico,
                              MoradorRepository repMorador,
                              PontoColetaRepository repPonto,
                              MaterialRepository repMaterial,
                              MaterialAceitoRepository repMaterialAceito,
                              OperadorRepository repOperador) {
        this.rep = rep;
        this.repItemSolicitacao = repItemSolicitacao;
        this.serviceHistorico = serviceHistorico;
        this.repMorador = repMorador;
        this.repPonto = repPonto;
        this.repMaterial = repMaterial;
        this.repMaterialAceito = repMaterialAceito;
        this.repOperador = repOperador;
    }
    @Transactional(readOnly = true)
    public Page<SolicitacaoResponseDTO> findAll(Pageable pageable) {
        Page<Solicitacao> solicitacoes = rep.findAll(pageable);
        return solicitacoes.map(solicitacao -> converterParaDTO(solicitacao));
    }
    @Transactional(readOnly = true)
    public SolicitacaoResponseDTO findById(Integer id) {
        Solicitacao solicitacao = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Solicitação não encontrada"));
        return converterParaDTO(solicitacao);
    }
    @Transactional(readOnly = true)
    public Page<SolicitacaoResponseDTO> findBySituacao(SituacaoSolicitacao situacao, Pageable pageable) {
        Page<Solicitacao> solicitacoes = rep.findBySituacao(situacao, pageable);
        return solicitacoes.map(solicitacao -> converterParaDTO(solicitacao));
    }
    @Transactional(readOnly = true)
    public Page<SolicitacaoResponseDTO> findByPontoColetaIdAndMoradorId(Integer idPonto, Integer idMorador, Pageable pageable) {
        Page<Solicitacao> solicitacoes = rep.findByPontoColeta_IdAndMorador_Id(idPonto, idMorador, pageable);
        return solicitacoes.map(solicitacao -> converterParaDTO(solicitacao));
    }
    @Transactional(readOnly = true)
    public Page<SolicitacaoResponseDTO> findByPontoColetaId(Integer idPonto, Pageable pageable) {
        Page<Solicitacao> solicitacoes = rep.findByPontoColeta_Id(idPonto, pageable);
        return solicitacoes.map(solicitacao -> converterParaDTO(solicitacao));
    }
    @Transactional(readOnly = true)
    public Page<SolicitacaoResponseDTO> findByPontoColetaIdAndSituacaoAndDataCriacaoBetween(Integer idPonto, SituacaoSolicitacao situacao,
                                                                                            LocalDateTime dataInicio,
                                                                                            LocalDateTime dataFim,
                                                                                            Pageable pageable) {
        Page<Solicitacao> solicitacoes = rep.findByPontoColeta_IdAndSituacaoAndDataCriacaoBetween(idPonto,
                situacao,
                dataInicio, dataFim, pageable);
        return solicitacoes.map(solicitacao -> converterParaDTO(solicitacao));
    }
    @Transactional(readOnly = true)
    public Page<SolicitacaoResponseDTO> findByPontoColetaIdAndDataCriacaoBetween(Integer idPonto, LocalDateTime dataInicio,
                                                                                 LocalDateTime dataFim, Pageable pageable) {
        Page<Solicitacao> solicitacoes = rep.findByPontoColeta_IdAndDataCriacaoBetween(idPonto, dataInicio, dataFim, pageable);
        return solicitacoes.map(solicitacao -> converterParaDTO(solicitacao));
    }
    @Transactional(readOnly = true)
    public Page<SolicitacaoResponseDTO> findByPontoColetaIdAndSituacao(Integer idPonto, SituacaoSolicitacao situacao, Pageable pageable) {
        Page<Solicitacao> solicitacoes = rep.findByPontoColeta_IdAndSituacao(idPonto, situacao, pageable);
        return solicitacoes.map(solicitacao -> converterParaDTO(solicitacao));
    }
    @Transactional(readOnly = true)
    public Page<SolicitacaoResponseDTO> findByMoradorId(Integer idMorador, Pageable pageable) {
        Page<Solicitacao> solicitacoes = rep.findByMorador_Id(idMorador, pageable);
        return solicitacoes.map(solicitacao -> converterParaDTO(solicitacao));
    }
    @Transactional(readOnly = true)
    public Page<SolicitacaoResponseDTO> findByMoradorIdAndSituacao(Integer idMorador, SituacaoSolicitacao situacao, Pageable pageable) {
        Page<Solicitacao> solicitacoes = rep.findByMorador_IdAndSituacao(idMorador, situacao, pageable);
        return solicitacoes.map(solicitacao -> converterParaDTO(solicitacao));
    }
    @Transactional(readOnly = true)
    public Page<SolicitacaoResponseDTO> findByMoradorIdAndSituacaoAndDataCriacaoBetween(Integer idMorador, SituacaoSolicitacao situacao,
                                                                                   LocalDateTime dataInicio, LocalDateTime dataFim, Pageable pageable) {
        Page<Solicitacao> solicitacoes = rep.findByMorador_IdAndSituacaoAndDataCriacaoBetween(idMorador, situacao, dataInicio, dataFim, pageable);
        return solicitacoes.map(solicitacao -> converterParaDTO(solicitacao));
    }
    @Transactional(readOnly = true)
    public Page<SolicitacaoResponseDTO> findByMoradorIdAndDataCriacaoBetween(Integer idMorador, LocalDateTime dataInicio,
                                                                             LocalDateTime dataFim, Pageable pageable) {
        Page<Solicitacao> solicitacoes = rep.findByMorador_IdAndDataCriacaoBetween(idMorador, dataInicio, dataFim, pageable);
        return solicitacoes.map(solicitacao -> converterParaDTO(solicitacao));
    }
    @Transactional(readOnly = true)
    public Page<SolicitacaoResponseDTO> findByDataCriacaoBetween(LocalDateTime dataInicio, LocalDateTime dataFim, Pageable pageable) {
        Page<Solicitacao> solicitacoes = rep.findByDataCriacaoBetween(dataInicio, dataFim, pageable);
        return solicitacoes.map(solicitacao -> converterParaDTO(solicitacao));
    }
    @Transactional(readOnly = true)
    public SolicitacaoResponseDTO findByProtocoloUnico(String protocolo) {
        Solicitacao solicitacao = rep.findByProtocoloUnico(protocolo)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Solicitação não encontrada"));
        return converterParaDTO(solicitacao);
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
            MaterialAceitoId idMatAceito = new MaterialAceitoId(ponto.getId(), material.getId());
            MaterialAceito materialAceito = repMaterialAceito.findById(idMatAceito)
                    .orElseThrow(() -> new RegraNegocioException("O material não está associado ao ponto de coleta."));
            if(materialAceito.getLimiteOpcional() != null && itemDTO.qtdEstimada().compareTo(materialAceito.getLimiteOpcional()) > 0) {
                throw new RegraNegocioException("A quantidade solicitada ultrapassa o limite aceito para este material.");
            }
            ItemSolicitacao item = ItemSolicitacao.builder()
                    //ainda n seto o atributo solicitacao pq a variavel dele
                    // ainda n foi criada nesse ponto, só mais embaixo, entao dps irei setar num
                    // for each que utilizarei mais abaixo
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
                null,
                "Solicitação criada",
                null);
        return converterParaDTO(solicitacao);
    }


    @Transactional
    public SolicitacaoResponseDTO atualizarSolicitacao (Integer id, SolicitacaoUpdateRequestDTO solicitacaodto,
                                                        Integer idOperador) {
        Solicitacao solicitacao = rep.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Solicitação não encontrada"));
        if (solicitacao.getSituacao() != SituacaoSolicitacao.SOLICITADA) {
            throw new RegraNegocioException("A solicitação não pode ser atualizada, pois não está mais na situação SOLICITADA.");
        }
        //usei ternario para poupar linha em vez de inicializar a variavel como null e fazer if else
        PontoColeta ponto = solicitacaodto.idPonto() != null ? repPonto.findById(solicitacaodto.idPonto())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ponto de coleta não encontrado")) :
                solicitacao.getPontoColeta();

        //defino a situacao do recolhimentoDomiciliar aqui para usar duas vezes abaixo,
        Boolean recDomiciliarFinal = solicitacaodto.recolhimentoDomiciliar() != null
                ? solicitacaodto.recolhimentoDomiciliar() : solicitacao.getRecolhimentoDomiciliar();

        //verificando se o pontoId gerado mais acima é igual ao antigo, se for diferente,
        //quer dizer então que o usuário passou um novo ponto, então é necessário validar as
        //coisas
        if (!Objects.equals(ponto.getId(), solicitacao.getPontoColeta().getId())) {
            if (ponto.getSituacao() != SituacaoPonto.ATIVO) {
                throw new RegraNegocioException("O ponto de coleta não está ativo.");
            }
            // uso o Boolean.TRUE.equals para prevenir NullPointerException usando Boolean
                if (Boolean.TRUE.equals(recDomiciliarFinal) && !Boolean.TRUE.equals(ponto.getRecolhimentoDomiciliar())) {
                    throw new RegraNegocioException("Este ponto de coleta não oferece recolhimento domiciliar.");
                }
        }
        solicitacao.setPontoColeta(ponto);
        //colocando oq o usuario mandou na lista para essa nova lista abaixo, caso nao mandou nada,
        // a lista é preenchida com os itens que ja tavam antes
        List<ItemSolicitacaoRequestDTO> itensParaValidar;
        if (solicitacaodto.itens() != null) {
            itensParaValidar = solicitacaodto.itens();
        } else {
            itensParaValidar = solicitacao.getItens().stream()
                    .map(item -> new ItemSolicitacaoRequestDTO(
                            item.getMaterial().getId(),
                            item.getQtdEstimada()
                    ))
                    .toList();
        }
        //caso o usuario só mande uma lista vazia
        if (itensParaValidar.isEmpty()) {
            throw new RegraNegocioException("A solicitação deve conter pelo menos um item.");
        }
        //mesma logica que tem no metodo de salvar, conferindo os itens, mesmo se não forem novos,
        // ou seja, se o ponto mudar, mas os itens continuarem
            List<ItemSolicitacao> itensValidados = new ArrayList<>();
            Set<Integer> materiaisAdicionados = new HashSet<>();
            for (ItemSolicitacaoRequestDTO itemDTO : itensParaValidar) {
                Material material = repMaterial.findById(itemDTO.idMaterial())
                        .orElseThrow(() -> new RecursoNaoEncontradoException("Material não encontrado."));
                if (material.getSituacao() != SituacaoMaterial.ATIVO) {
                    throw new RegraNegocioException("O material não está ativo e não pode ser solicitado.");
                }
                if (!materiaisAdicionados.add(material.getId())) {
                    throw new RegraNegocioException("O mesmo material não pode ser adicionado duas vezes à solicitação.");
                }
                if (itemDTO.qtdEstimada() == null || itemDTO.qtdEstimada().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new RegraNegocioException("A quantidade estimada do material deve ser maior que zero.");
                }
                MaterialAceitoId idMatAceito = new MaterialAceitoId(ponto.getId(), material.getId());
                MaterialAceito materialAceito = repMaterialAceito.findById(idMatAceito)
                        .orElseThrow(() -> new RegraNegocioException("O material não está associado ao ponto de coleta."));
                if (materialAceito.getLimiteOpcional() != null && itemDTO.qtdEstimada().compareTo(materialAceito.getLimiteOpcional()) > 0) {
                    throw new RegraNegocioException("A quantidade solicitada ultrapassa o limite aceito para este material.");
                }
                //so entra nessas se a lista for nova
                if (solicitacaodto.itens() != null) {
                    ItemSolicitacao item = ItemSolicitacao.builder()
                            //diferente do salvarSolicit, aqui ja da pra setar
                            //solicitacao, ja que ja foi declarada la em cima
                            .solicitacao(solicitacao)
                            .material(material)
                            .copiaUnidade(material.getUnidade())
                            .qtdEstimada(itemDTO.qtdEstimada())
                            .build();
                    itensValidados.add(item);
                }
            }
        //se tiver sido uma lista nova deleta todos os itens antigos pra depois adicionar os novos
            if (solicitacaodto.itens() != null) {
                repItemSolicitacao.deleteAllBySolicitacao_Id(solicitacao.getId());
                solicitacao.getItens().clear();
                for (ItemSolicitacao item : itensValidados) {
                    solicitacao.getItens().add(item);
                    repItemSolicitacao.save(item);
                }
            }
        if(solicitacaodto.copiaLogradouro() != null) {
            solicitacao.setCopiaLogradouro(solicitacaodto.copiaLogradouro());
        }
        if(solicitacaodto.copiaNumero() != null) {
            solicitacao.setCopiaNumero(solicitacaodto.copiaNumero());
        }
        if(solicitacaodto.copiaComplemento() != null) {
            solicitacao.setCopiaComplemento(solicitacaodto.copiaComplemento());
        }
        if(solicitacaodto.copiaBairro() != null) {
            solicitacao.setCopiaBairro(solicitacaodto.copiaBairro());
        }
        if(solicitacaodto.copiaMunicipio() != null) {
            solicitacao.setCopiaMunicipio(solicitacaodto.copiaMunicipio());
        }
        if(solicitacaodto.copiaUf() != null) {
            solicitacao.setCopiaUf(solicitacaodto.copiaUf());
        }
        if(solicitacaodto.copiaCep() != null) {
            solicitacao.setCopiaCep(solicitacaodto.copiaCep());
        }
        if(solicitacaodto.observacoes() != null) {
            solicitacao.setObservacoes(solicitacaodto.observacoes());
        }
        //verifica se o ponto aceita recolhimento domiciliar se o usuario tiver mudado na solicitacao
        if (Boolean.TRUE.equals(recDomiciliarFinal) && !Boolean.TRUE.equals(solicitacao.getPontoColeta().getRecolhimentoDomiciliar())) {
            throw new RegraNegocioException("Este ponto de coleta não oferece recolhimento domiciliar.");
        }
        solicitacao.setRecolhimentoDomiciliar(recDomiciliarFinal);
        rep.save(solicitacao);
        SituacaoSolicitacao situacaoAntiga = solicitacao.getSituacao();
        serviceHistorico.registrarHistorico(
                solicitacao.getId(),
                situacaoAntiga.name(),
                solicitacao.getSituacao().name(),
                idOperador,
                "Solicitação atualizada",
                null);
        return converterParaDTO(solicitacao);
    }
    @Transactional
    public SolicitacaoResponseDTO agendarSolicitacao (Integer id, AgendamentoRequestDTO agendamentodto, Integer idOperador) {
        Solicitacao solicitacao = rep.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Solicitação não encontrada"));
        if (solicitacao.getSituacao() != SituacaoSolicitacao.SOLICITADA) {
            throw new RegraNegocioException("A solicitação não pode ser agendada, pois não está mais na situação SOLICITADA.");
        }
        Operador operador = repOperador.findById(idOperador).orElseThrow(() -> new RecursoNaoEncontradoException("Operador não encontrado"));
        if (!operador.getPontoColeta().getId().equals(solicitacao.getPontoColeta().getId())) {
            throw new RegraNegocioException("Operador não pertence ao ponto de coleta desta solicitação");
        }
        if (agendamentodto.dataAgendamento() == null || !agendamentodto.dataAgendamento().isAfter(LocalDateTime.now())) {
            throw new RegraNegocioException("A data do agendamento deve ser futura.");
        }
        solicitacao.setDataAgendamento(agendamentodto.dataAgendamento());
        SituacaoSolicitacao situacaoAntiga = solicitacao.getSituacao();
        solicitacao.setSituacao(SituacaoSolicitacao.AGENDADA);
        rep.save(solicitacao);
        serviceHistorico.registrarHistorico(
                solicitacao.getId(),
                situacaoAntiga.name(),
                solicitacao.getSituacao().name(),
                idOperador,
                "Solicitação agendada",
                null);
        return converterParaDTO(solicitacao);
    }

    @Transactional
    public SolicitacaoResponseDTO recusarSolicitacao (Integer id,  String motivo, Integer idOperador) {
        Solicitacao solicitacao = rep.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Solicitação não encontrada"));
        if (solicitacao.getSituacao() != SituacaoSolicitacao.SOLICITADA) {
            throw new RegraNegocioException("A solicitação não pode ser recusada, pois não está mais na situação " +
                    "SOLICITADA.");
        }
        Operador operador = repOperador.findById(idOperador).orElseThrow(() -> new RecursoNaoEncontradoException("Operador não encontrado"));
        if (!operador.getPontoColeta().getId().equals(solicitacao.getPontoColeta().getId())) {
            throw new RegraNegocioException("Operador não pertence ao ponto de coleta desta solicitação");
        }
        if (motivo == null || motivo.isBlank()) {throw new RegraNegocioException("O motivo da recusa é obrigatório.");
        }
        SituacaoSolicitacao situacaoAntiga = solicitacao.getSituacao();
        solicitacao.setSituacao(SituacaoSolicitacao.RECUSADA);
        rep.save(solicitacao);
        serviceHistorico.registrarHistorico(
                solicitacao.getId(),
                situacaoAntiga.name(),
                solicitacao.getSituacao().name(),
                idOperador,
                motivo,
                null);
        return converterParaDTO(solicitacao);
    }

    @Transactional
    public SolicitacaoResponseDTO iniciarRecolhimento(Integer id, Integer idOperador) {
        Solicitacao solicitacao = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Solicitação não encontrada"));
        if (solicitacao.getSituacao() != SituacaoSolicitacao.AGENDADA) {
            throw new RegraNegocioException("A solicitação não pode ser iniciada, pois não está mais na situação AGENDADA.");
        }
        Operador operador = repOperador.findById(idOperador).orElseThrow(() -> new RecursoNaoEncontradoException("Operador não encontrado"));
        if (!operador.getPontoColeta().getId().equals(solicitacao.getPontoColeta().getId())) {
            throw new RegraNegocioException("Operador não pertence ao ponto de coleta desta solicitação");
        }
        SituacaoSolicitacao situacaoAntiga = solicitacao.getSituacao();
        solicitacao.setSituacao(SituacaoSolicitacao.EM_RECOLHIMENTO);
        rep.save(solicitacao);
        serviceHistorico.registrarHistorico(
                solicitacao.getId(),
                situacaoAntiga.name(),
                solicitacao.getSituacao().name(),
                idOperador,
                "Solicitação iniciada (Em recolhimento)",
                null);
        return converterParaDTO(solicitacao);
    }
    @Transactional
    public SolicitacaoResponseDTO cancelarSolicitacao(Integer id, String motivo, Integer idOperador) {
        Solicitacao solicitacao = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Solicitação não encontrada"));
        if (solicitacao.getSituacao() != SituacaoSolicitacao.AGENDADA && solicitacao.getSituacao() != SituacaoSolicitacao.SOLICITADA) {
            throw new RegraNegocioException("A solicitação não pode ser cancelada, pois não está na situação " +
                    "SOLICITADA ou AGENDADA.");
        }
        Operador operador = repOperador.findById(idOperador).orElseThrow(() -> new RecursoNaoEncontradoException("Operador não encontrado"));
        if (!operador.getPontoColeta().getId().equals(solicitacao.getPontoColeta().getId())) {
            throw new RegraNegocioException("Operador não pertence ao ponto de coleta desta solicitação");
        }
        if (motivo == null || motivo.isBlank()) {
            throw new RegraNegocioException("O motivo do cancelamento é obrigatório.");
        }
        SituacaoSolicitacao situacaoAntiga = solicitacao.getSituacao();
        solicitacao.setSituacao(SituacaoSolicitacao.CANCELADA);
        rep.save(solicitacao);
        serviceHistorico.registrarHistorico(
                solicitacao.getId(),
                situacaoAntiga.name(),
                solicitacao.getSituacao().name(),
                idOperador,
                motivo,
                null);
        return converterParaDTO(solicitacao);
    }

    @Transactional
    public SolicitacaoResponseDTO concluirSolicitacao(Integer id, ConclusaoRequestDTO conclusaodto,
                                                      Integer idOperador) {
        Solicitacao solicitacao = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Solicitação não encontrada"));
        if (solicitacao.getSituacao() != SituacaoSolicitacao.EM_RECOLHIMENTO) {
            throw new RegraNegocioException("A solicitação não pode ser concluída, pois não está na situação EM " +
                    "RECOLHIMENTO.");
        }
        Operador operador = repOperador.findById(idOperador).orElseThrow(() -> new RecursoNaoEncontradoException("Operador não encontrado"));
        if(!operador.getPontoColeta().getId().equals(solicitacao.getPontoColeta().getId())) {
            throw new RegraNegocioException("Operador não pertence ao ponto de coleta desta solicitação");
        }
        if(conclusaodto.itens() == null || conclusaodto.itens().isEmpty()) {
            throw new RegraNegocioException("A solicitação deve conter pelo menos um item.");
        }
        List<ItemSolicitacao> itensValidados = new ArrayList<>();
        Set<Integer> materiaisAdicionados = new HashSet<>();
        //atendendo aos requisitos do projeto, que pelo menos um item tem que ter a
        //quantidade efetiva positiva
        boolean existePositivo = false;
        for(ItemConclusaoRequestDTO item : conclusaodto.itens()){
            ItemSolicitacao itemSolicitacao = repItemSolicitacao.findBySolicitacao_IdAndMaterial_Id(solicitacao.getId(), item.idMaterial())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Item da solicitação não encontrado"));
            if(item.quantidadeEfetiva() == null || item.quantidadeEfetiva().compareTo(BigDecimal.ZERO) < 0) {
                throw new RegraNegocioException("A quantidade efetiva do material deve ser maior ou igual a zero.");
            }
            if(!materiaisAdicionados.add(item.idMaterial())) {
                throw new RegraNegocioException("O mesmo material não pode ser adicionado duas vezes à conclusão da solicitação.");
            }
            if (item.quantidadeEfetiva().compareTo(BigDecimal.ZERO) > 0) {
                existePositivo = true;
            }
            itemSolicitacao.setQtdEfetiva(item.quantidadeEfetiva());
            itensValidados.add(itemSolicitacao);
            //e assim, com a conclusao da solicitacao,
            // a tabela ItemSolicitacao é preenchida completamente
        }
        if (!existePositivo) {
            throw new RegraNegocioException("A solicitação deve conter pelo menos um item com quantidade efetiva positiva.");
        }
        if (conclusaodto.itens().size() != solicitacao.getItens().size()) {
            throw new RegraNegocioException("É necessário informar a quantidade efetiva de todos os itens da solicitação.");
        }
        repItemSolicitacao.saveAll(itensValidados);
        SituacaoSolicitacao situacaoAnterior = solicitacao.getSituacao();
        solicitacao.setSituacao(SituacaoSolicitacao.CONCLUIDA);
        solicitacao.setDataConclusao(LocalDateTime.now());
        rep.save(solicitacao);
        serviceHistorico.registrarHistorico(
                solicitacao.getId(),
                situacaoAnterior.name(),
                solicitacao.getSituacao().name(),
                idOperador,
                "Solicitação concluída",
                null);
        return converterParaDTO(solicitacao);
    }
    @Transactional
    public SolicitacaoResponseDTO reagendarSolicitacao(Integer id, String motivo,
                                                       String infoTentativa, AgendamentoRequestDTO agendamentodto,
                                                       Integer idOperador) {
        Solicitacao solicitacao = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Solicitação não encontrada"));
        if (solicitacao.getSituacao() != SituacaoSolicitacao.EM_RECOLHIMENTO && solicitacao.getSituacao() != SituacaoSolicitacao.AGENDADA) {
            throw new RegraNegocioException("A solicitação não pode ser reagendada, pois não está na situação " +
                    "EM RECOLHIMENTO ou AGENDADA.");
        }
        Operador operador = repOperador.findById(idOperador).orElseThrow(() -> new RecursoNaoEncontradoException("Operador não encontrado"));
        if (!operador.getPontoColeta().getId().equals(solicitacao.getPontoColeta().getId())) {
            throw new RegraNegocioException("Operador não pertence ao ponto de coleta desta solicitação");
        }
        if (agendamentodto.dataAgendamento() == null || !agendamentodto.dataAgendamento().isAfter(LocalDateTime.now())) {
            throw new RegraNegocioException("A nova data do agendamento deve ser futura.");
        }
        if (motivo == null || motivo.isBlank()) {
            throw new RegraNegocioException("A justificativa do reagendamento é obrigatória.");
        }
        if (infoTentativa == null || infoTentativa.isBlank()) {throw new RegraNegocioException("A informação da tentativa é " +
                "obrigatória.");
        }
        SituacaoSolicitacao situacaoAntiga = solicitacao.getSituacao();
        solicitacao.setSituacao(SituacaoSolicitacao.AGENDADA);
        solicitacao.setDataAgendamento(agendamentodto.dataAgendamento());
        rep.save(solicitacao);
        serviceHistorico.registrarHistorico(
                solicitacao.getId(),
                situacaoAntiga.name(),
                solicitacao.getSituacao().name(),
                idOperador,
                motivo,
                infoTentativa);
        return converterParaDTO(solicitacao);
    }

    //nessa conversao para dto, pego uma lista de ItemSolicitacao e atribuo a ela
    //o a lista atributo Itens da solicitacao, depois de fazer a conversão com stream,
    // uso essa lista para retornar ela junto com os outros campos da SolicitacaoResponseDTO
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
