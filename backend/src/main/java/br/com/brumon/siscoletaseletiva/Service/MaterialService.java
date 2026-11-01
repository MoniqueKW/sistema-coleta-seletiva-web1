package br.com.brumon.siscoletaseletiva.Service;

import br.com.brumon.siscoletaseletiva.DTO.Response.MaterialResponseDTO;
import br.com.brumon.siscoletaseletiva.Entity.Material;
import br.com.brumon.siscoletaseletiva.Enum.SituacaoMaterial;
import br.com.brumon.siscoletaseletiva.Exception.RecursoNaoEncontradoException;
import br.com.brumon.siscoletaseletiva.Repository.MaterialRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    //continuar
}
