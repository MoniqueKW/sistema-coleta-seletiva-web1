package br.com.brumon.siscoletaseletiva.service;

import br.com.brumon.siscoletaseletiva.dto.request.AlterarSituacaoMaterialRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.request.MaterialRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.response.MaterialResponseDTO;
import br.com.brumon.siscoletaseletiva.model.entity.Material;
import br.com.brumon.siscoletaseletiva.model.enums.SituacaoMaterial;
import br.com.brumon.siscoletaseletiva.exception.RecursoDuplicadoException;
import br.com.brumon.siscoletaseletiva.exception.RecursoNaoEncontradoException;
import br.com.brumon.siscoletaseletiva.exception.RegraNegocioException;
import br.com.brumon.siscoletaseletiva.repository.ItemSolicitacaoRepository;
import br.com.brumon.siscoletaseletiva.repository.MaterialAceitoRepository;
import br.com.brumon.siscoletaseletiva.repository.MaterialRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MaterialService {
    private final MaterialRepository rep;
    private final ItemSolicitacaoRepository repItemSolicitacao;
    private final MaterialAceitoRepository repMaterialAceito;
    public MaterialService(MaterialRepository rep,
                           ItemSolicitacaoRepository repItemSolicitacao,
                           MaterialAceitoRepository repMaterialAceito) {
        this.rep = rep;
        this.repItemSolicitacao = repItemSolicitacao;
        this.repMaterialAceito = repMaterialAceito;
    }

    @Transactional(readOnly = true)
    public Page<MaterialResponseDTO> findAll(Pageable pageable) {
        Page<Material> materiais = rep.findAll(pageable);
        return materiais.map(this::converterParaDTO);
    }

    @Transactional(readOnly = true)
    public Page<MaterialResponseDTO> findByNome(String nome, Pageable pageable) {
        Page<Material> materiais = rep.findByNomeContainingIgnoreCase(nome, pageable);
        return materiais.map(this::converterParaDTO);
    }

    @Transactional(readOnly = true)
    public MaterialResponseDTO findById(Integer id) {
        Material material = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Material não encontrado"));
        return converterParaDTO(material);
    }

    @Transactional(readOnly = true)
    public Page<MaterialResponseDTO> findBySituacao(SituacaoMaterial situacao, Pageable pageable) {
        Page<Material> materiais = rep.findBySituacao(situacao, pageable);
        return materiais.map(this::converterParaDTO);
    }

    @Transactional
    public MaterialResponseDTO salvarMaterial(MaterialRequestDTO materialdto) {
        if (rep.existsByNomeIgnoreCase(materialdto.nome())) {
            throw new RecursoDuplicadoException("Material com nome já cadastrado. Este material já existe.");
        }
        Material material = rep.save(Material.builder()
                .nome(materialdto.nome())
                .descricao(materialdto.descricao())
                .unidade(materialdto.unidade())
                .orientacao(materialdto.orientacao())
                .situacao(SituacaoMaterial.ATIVO)
                .build());
        return converterParaDTO(material);
    }

    @Transactional
    public MaterialResponseDTO atualizarMaterial(MaterialRequestDTO materialdto, Integer id) {
        Material material = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Material não encontrado"));

        // Verifica se o nome foi alterado E se o novo nome já existe exatamente igual no banco
        if (!material.getNome().equalsIgnoreCase(materialdto.nome()) && rep.existsByNomeIgnoreCase(materialdto.nome())) {
            throw new RecursoDuplicadoException("Material com nome já cadastrado. Este material já existe.");
        }

        material.setNome(materialdto.nome());
        material.setUnidade(materialdto.unidade());
        material.setDescricao(materialdto.descricao());
        material.setOrientacao(materialdto.orientacao());

        Material materialSalvo = rep.save(material);
        return converterParaDTO(materialSalvo);
    }

    @Transactional
    public MaterialResponseDTO alterarSituacao(Integer id, AlterarSituacaoMaterialRequestDTO situacaodto){
        Material material = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Material não encontrado"));
        material.setSituacao(situacaodto.situacao());
        Material materialSalvo = rep.save(material);
        return converterParaDTO(material);
    }

    //apaga se nao tiver associacoes (ou seja, so pra testar, ja que quase sempre
    // vai ter associacao a Ponto e/ou Solicitacao)
    @Transactional
    public void deleteById(Integer idMaterial) {
        Material material = rep.findById(idMaterial)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Material não encontrado."));
        boolean possuiAssociacoes = repMaterialAceito.existsByMaterial_Id(idMaterial);
        if (possuiAssociacoes) {
            throw new RegraNegocioException("Não é possível excluir o material, pois existem associações com ele.");
        }
        boolean possuiSolicitacoes = repItemSolicitacao.existsByMaterial_Id(idMaterial);
        if (possuiSolicitacoes) {
            throw new RegraNegocioException("Não é possível excluir o material, pois existem solicitações associadas a ele.");
        }
        rep.delete(material);
    }

    public MaterialResponseDTO converterParaDTO(Material material) {
        return new MaterialResponseDTO(
                material.getId(),
                material.getNome(),
                material.getDescricao(),
                material.getUnidade(),
                material.getOrientacao(),
                material.getSituacao()
        );
    }
}
