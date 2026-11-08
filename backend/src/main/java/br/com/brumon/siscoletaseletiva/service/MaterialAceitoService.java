package br.com.brumon.siscoletaseletiva.service;

import br.com.brumon.siscoletaseletiva.dto.request.MaterialAceitoRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.request.MaterialAceitoUpdateRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.response.MaterialAceitoResponseDTO;
import br.com.brumon.siscoletaseletiva.model.entity.Material;
import br.com.brumon.siscoletaseletiva.model.entity.MaterialAceito;
import br.com.brumon.siscoletaseletiva.model.entity.MaterialAceitoId;
import br.com.brumon.siscoletaseletiva.model.entity.PontoColeta;
import br.com.brumon.siscoletaseletiva.model.enums.SituacaoMaterial;
import br.com.brumon.siscoletaseletiva.model.enums.SituacaoSolicitacao;
import br.com.brumon.siscoletaseletiva.exception.RecursoDuplicadoException;
import br.com.brumon.siscoletaseletiva.exception.RecursoNaoEncontradoException;
import br.com.brumon.siscoletaseletiva.exception.RegraNegocioException;
import br.com.brumon.siscoletaseletiva.repository.ItemSolicitacaoRepository;
import br.com.brumon.siscoletaseletiva.repository.MaterialAceitoRepository;
import br.com.brumon.siscoletaseletiva.repository.MaterialRepository;
import br.com.brumon.siscoletaseletiva.repository.PontoColetaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MaterialAceitoService {
    private final MaterialAceitoRepository rep;
    private final MaterialRepository repMaterial;
    private final PontoColetaRepository repPonto;
    private final ItemSolicitacaoRepository repItemSolicitacao;
    public MaterialAceitoService(
            MaterialAceitoRepository rep,
            MaterialRepository repMaterial,
            PontoColetaRepository repPonto,
            ItemSolicitacaoRepository repItemSolicitacao
    ) {
        this.rep = rep;
        this.repMaterial = repMaterial;
        this.repPonto = repPonto;
        this.repItemSolicitacao = repItemSolicitacao;
    }
    @Transactional(readOnly = true)
    public Page<MaterialAceitoResponseDTO> findByIdPonto(Integer idPonto, Pageable pageable) {
        Page<MaterialAceito> materiaisAceitos = rep.findByPontoColeta_Id(idPonto, pageable);
        return materiaisAceitos.map(this::converterParaDTO);
    }
    @Transactional
    public MaterialAceitoResponseDTO associarMaterial(Integer idPonto, MaterialAceitoRequestDTO dto) {
        PontoColeta ponto = repPonto.findById(idPonto)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ponto de coleta não encontrado"));
        Material material = repMaterial.findById(dto.idMaterial())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Material não encontrado"));

        if (rep.existsByPontoColeta_IdAndMaterial_Id(idPonto, dto.idMaterial())) {
            throw new RecursoDuplicadoException("O material informado já está associado ao ponto de coleta");
        }
        //O Enum não pode ser comparado com string usando .equals, sempre resulta em falso.
        if (material.getSituacao() == SituacaoMaterial.INATIVO) {
            throw new RegraNegocioException("O material informado está inativo e não pode ser associado ao ponto de coleta");
        }
        //usando BigDecimal.Zero pq não dá para comparar BigDecimal com Int. Como no DTO
        // já estamos verificando isso com o @Positive, daria para apagar essa condição
        //aqui abaixo para evitar excesso de código, mas eu queria usar a
        //RegraNegocioException, então deixei assim mesmo
        if(dto.limiteOpcional() != null && dto.limiteOpcional().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraNegocioException("O limite opcional deve ser maior que zero");
        }

        MaterialAceito materialAceito = rep.save(MaterialAceito.builder()
                .id(new MaterialAceitoId(idPonto, material.getId())) //Precisa da chave composta
                .pontoColeta(ponto)
                .material(material)
                .observacoes(dto.observacoes())
                .limiteOpcional(dto.limiteOpcional())
                .build());

        return converterParaDTO(materialAceito);
    }

    @Transactional
    public MaterialAceitoResponseDTO atualizarMaterialAceito(Integer idPonto,
                                                             Integer idMaterial,
                                                             MaterialAceitoUpdateRequestDTO dto) {
        MaterialAceito materialAceito = rep.findByPontoColeta_IdAndMaterial_Id(idPonto, idMaterial)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Associação de material não encontrada"));
        materialAceito.setObservacoes(dto.observacoes());
        materialAceito.setLimiteOpcional(dto.limiteOpcional());
        MaterialAceito salvo = rep.save(materialAceito);
        return converterParaDTO(salvo);
    }
    @Transactional
    public void deleteAssociacao(Integer idPonto, Integer idMaterial) {
        MaterialAceito materialAceito = rep.findById(new MaterialAceitoId(idPonto, idMaterial))
                .orElseThrow(() -> new RecursoNaoEncontradoException("Associação de material não encontrada"));
        List<SituacaoSolicitacao> situacoesAbertas = List.of(
                SituacaoSolicitacao.AGENDADA,
                SituacaoSolicitacao.SOLICITADA,
                SituacaoSolicitacao.EM_RECOLHIMENTO
        );
        boolean materialEmUso = repItemSolicitacao
                .existsBySolicitacao_PontoColeta_IdAndSolicitacao_SituacaoInAndMaterial_Id(
                        idPonto,
                        situacoesAbertas,
                        idMaterial
                );
        if (materialEmUso) {
            throw new RegraNegocioException("Não é possível excluir a associação de material, pois existem solicitações em andamento para este material no ponto de coleta.");
        }
        rep.delete(materialAceito);
    }

    public MaterialAceitoResponseDTO converterParaDTO(MaterialAceito materialAceito) {
        return new MaterialAceitoResponseDTO(
                materialAceito.getPontoColeta().getId(),
                materialAceito.getMaterial().getId(),
                materialAceito.getMaterial().getNome(),
                materialAceito.getObservacoes(),
                materialAceito.getLimiteOpcional()
        );
    }
}
