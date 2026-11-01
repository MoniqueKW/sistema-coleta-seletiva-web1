package br.com.brumon.siscoletaseletiva.Service;

import br.com.brumon.siscoletaseletiva.DTO.Request.AlterarSituacaoMaterialRequestDTO;
import br.com.brumon.siscoletaseletiva.DTO.Request.MaterialRequestDTO;
import br.com.brumon.siscoletaseletiva.DTO.Response.MaterialResponseDTO;
import br.com.brumon.siscoletaseletiva.Entity.Material;
import br.com.brumon.siscoletaseletiva.Enum.SituacaoMaterial;
import br.com.brumon.siscoletaseletiva.Exception.RecursoDuplicadoException;
import br.com.brumon.siscoletaseletiva.Exception.RecursoNaoEncontradoException;
import br.com.brumon.siscoletaseletiva.Repository.MaterialRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialService {
    private final MaterialRepository rep;
    public MaterialService(MaterialRepository rep) {
        this.rep = rep;
    }

    public List<MaterialResponseDTO> findAll() {
        List<Material> lista = rep.findAll();
        if (lista.isEmpty()) {
            throw new RecursoNaoEncontradoException("Não há materiais cadastrados");
        }
        return lista.stream()
                .map(material -> new MaterialResponseDTO(
                        material.getId(),
                        material.getNome(),
                        material.getUnidade(),
                        material.getDescricao(),
                        material.getOrientacao(),
                        material.getSituacao())
                )
                .toList();
    }
    public MaterialResponseDTO findByNome(String nome) {
        Material material = rep.findByNomeContainingIgnoreCase(nome).orElseThrow(() -> new RecursoNaoEncontradoException("Material não encontrado"));
        return new MaterialResponseDTO(
                material.getId(),
                material.getNome(),
                material.getUnidade(),
                material.getDescricao(),
                material.getOrientacao(),
                material.getSituacao()
        );
    }
    public MaterialResponseDTO findById(Integer id) {
        Material material = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Material não encontrado"));
        return new MaterialResponseDTO(
                material.getId(),
                material.getNome(),
                material.getUnidade(),
                material.getDescricao(),
                material.getOrientacao(),
                material.getSituacao()
        );
    }
    public List<MaterialResponseDTO> findBySituacao(SituacaoMaterial situacao) {
        List<Material> lista = rep.findBySituacao(situacao);
        if (lista.isEmpty()) {
            throw new RecursoNaoEncontradoException("Material não encontrado");
        }
        return lista.stream()
                .map(material -> new MaterialResponseDTO(
                        material.getId(),
                        material.getNome(),
                        material.getUnidade(),
                        material.getDescricao(),
                        material.getOrientacao(),
                        material.getSituacao()
                ))
                .toList();
    }
    @Transactional
    public MaterialResponseDTO salvarMaterial(MaterialRequestDTO materialdto) {
        if (rep.findByNomeContainingIgnoreCase(materialdto.nome()).isPresent()) {
            throw new RecursoDuplicadoException("Material com nome já cadastrado. Este material já existe.");
        }
        Material material = rep.save(Material.builder()
                .nome(materialdto.nome())
                .descricao(materialdto.descricao())
                .unidade(materialdto.unidade())
                .orientacao(materialdto.orientacao())
                .situacao(SituacaoMaterial.ATIVO)
                .build());
        return new MaterialResponseDTO(
                material.getId(),
                material.getNome(),
                material.getUnidade(),
                material.getDescricao(),
                material.getOrientacao(),
                material.getSituacao()
        );
    }
    @Transactional
    public MaterialResponseDTO atualizarMaterial(MaterialRequestDTO materialdto, Integer id) {
        Material material = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Material não encontrado"));
        if (!material.getNome().equalsIgnoreCase(materialdto.nome()) && rep.findByNomeContainingIgnoreCase(materialdto.nome()).isPresent()) {
            throw new RecursoDuplicadoException("Material com nome já cadastrado. Este material já existe.");
        }
        material = material.builder()
                .nome(materialdto.nome())
                .descricao(materialdto.descricao())
                .unidade(materialdto.unidade())
                .orientacao(materialdto.orientacao())
                .situacao(material.getSituacao())
                .build();
        Material materialSalvo = rep.save(material);
        return new MaterialResponseDTO(
                materialSalvo.getId(),
                materialSalvo.getNome(),
                materialSalvo.getUnidade(),
                materialSalvo.getDescricao(),
                materialSalvo.getOrientacao(),
                materialSalvo.getSituacao()
        );
    }
    @Transactional
    public MaterialResponseDTO alterarSituacao(Integer id, AlterarSituacaoMaterialRequestDTO situacaodto){
        Material material = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Material não encontrado"));
        material.setSituacao(situacaodto.situacao());
        Material materialSalvo = rep.save(material);
        return new MaterialResponseDTO(
                materialSalvo.getId(),
                materialSalvo.getNome(),
                materialSalvo.getUnidade(),
                materialSalvo.getDescricao(),
                materialSalvo.getOrientacao(),
                materialSalvo.getSituacao()
        );
    }
    public void deleteById(Integer id) {
        if (rep.findById(id).isPresent()) {
            rep.deleteById(id);
        } else {
            throw new RecursoNaoEncontradoException("Material não encontrado");
        }
    }
    @Transactional
    public void deleteByNome(String nome) {
        if (rep.findByNomeContainingIgnoreCase(nome).isPresent()) {
            rep.deleteByNome(nome);
        } else {
            throw new RecursoNaoEncontradoException("Material não encontrado");
        }
    }
}
