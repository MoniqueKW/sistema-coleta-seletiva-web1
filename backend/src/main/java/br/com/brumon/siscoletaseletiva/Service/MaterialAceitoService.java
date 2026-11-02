package br.com.brumon.siscoletaseletiva.Service;

import br.com.brumon.siscoletaseletiva.DTO.Request.MaterialAceitoRequestDTO;
import br.com.brumon.siscoletaseletiva.DTO.Request.MaterialAceitoUpdateRequestDTO;
import br.com.brumon.siscoletaseletiva.DTO.Response.MaterialAceitoResponseDTO;
import br.com.brumon.siscoletaseletiva.Entity.Material;
import br.com.brumon.siscoletaseletiva.Entity.MaterialAceito;
import br.com.brumon.siscoletaseletiva.Entity.PontoColeta;
import br.com.brumon.siscoletaseletiva.Exception.RecursoDuplicadoException;
import br.com.brumon.siscoletaseletiva.Exception.RecursoNaoEncontradoException;
import br.com.brumon.siscoletaseletiva.Exception.RegraNegocioException;
import br.com.brumon.siscoletaseletiva.Repository.MaterialAceitoRepository;
import br.com.brumon.siscoletaseletiva.Repository.MaterialRepository;
import br.com.brumon.siscoletaseletiva.Repository.PontoColetaRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MaterialAceitoService {
    private final MaterialAceitoRepository rep;
    private final MaterialRepository repMaterial;
    private final PontoColetaRepository repPonto;
    public MaterialAceitoService(
            MaterialAceitoRepository rep,
            MaterialRepository repMaterial,
            PontoColetaRepository repPonto
    ) {
        this.rep = rep;
        this.repMaterial = repMaterial;
        this.repPonto = repPonto;
    }
    public Page<MaterialAceitoResponseDTO> findAll(Pageable pageable) {
        Page<MaterialAceito> materialAceitos = rep.findAll(pageable);
        if (materialAceitos.isEmpty()) {
            throw new RecursoNaoEncontradoException("Não há materiais aceitos cadastrados");
        }
        return materialAceitos.map(materialAceito -> new MaterialAceitoResponseDTO(
                materialAceito.getPontoColeta().getId(),
                materialAceito.getMaterial().getId(),
                materialAceito.getMaterial().getNome(),
                materialAceito.getObservacoes(),
                materialAceito.getLimiteOpcional()
        ));
    }
    public Page<MaterialAceitoResponseDTO> findByIdPonto(Integer idPonto, Pageable pageable) {
        Page<MaterialAceito> materialAceitos = rep.findByPontoColeta_Id(idPonto, pageable);
        if (materialAceitos.isEmpty()) {
            throw new RecursoNaoEncontradoException("Não há materiais aceitos cadastrados para o ponto de coleta informado");
        }
        return materialAceitos.map(materialAceito -> new MaterialAceitoResponseDTO(
                materialAceito.getPontoColeta().getId(),
                materialAceito.getMaterial().getId(),
                materialAceito.getMaterial().getNome(),
                materialAceito.getObservacoes(),
                materialAceito.getLimiteOpcional()
        ));
    }
    @Transactional
    public MaterialAceitoResponseDTO associarMaterial(Integer idPonto, MaterialAceitoRequestDTO dto) {
        PontoColeta ponto = repPonto.findById(idPonto).orElseThrow(() -> new RecursoNaoEncontradoException("Ponto de coleta não encontrado"));
        Material material = repMaterial.findById(dto.idMaterial()).orElseThrow(() -> new RecursoNaoEncontradoException("Material não encontrado"));
        if (rep.existsByPontoColeta_IdAndMaterial_Id(idPonto, dto.idMaterial())) {
            throw new RecursoDuplicadoException("O material informado já está associado ao ponto de coleta");
        }
        if (material.getSituacao().equals("Inativo")) {
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
                .pontoColeta(ponto)
                .material(material)
                .observacoes(dto.observacoes())
                .limiteOpcional(dto.limiteOpcional())
                .build());
        return new MaterialAceitoResponseDTO(
                materialAceito.getPontoColeta().getId(),
                materialAceito.getMaterial().getId(),
                materialAceito.getMaterial().getNome(),
                materialAceito.getObservacoes(),
                materialAceito.getLimiteOpcional()
        );
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
        return new MaterialAceitoResponseDTO(
                salvo.getPontoColeta().getId(),
                salvo.getMaterial().getId(),
                salvo.getMaterial().getNome(),
                salvo.getObservacoes(),
                salvo.getLimiteOpcional()
        );
    }
    @Transactional
    public void deleteByPontoColeta_IdAndMaterial_Id(Integer idPonto, Integer idMaterial) {
        if (rep.existsByPontoColeta_IdAndMaterial_Id(idPonto, idMaterial)) {
            rep.deleteByPontoColeta_IdAndMaterial_Id(idPonto, idMaterial);
        } else {
            throw new RecursoNaoEncontradoException("Associação de material não encontrada");
        }
    }
    @Transactional
    public MaterialAceitoResponseDTO alterarAssociacao(Integer idPonto, Integer idMaterial, MaterialAceitoRequestDTO dto) {
        if(dto.idMaterial() == idMaterial) {
            throw new RecursoDuplicadoException("Não é possível alterar a associação para o mesmo material. Informe um material diferente.");
        }
        if(rep.existsByPontoColeta_IdAndMaterial_Id(idPonto,idMaterial)) {
            deleteByPontoColeta_IdAndMaterial_Id(idPonto,idMaterial);
        } else {
            throw new RecursoNaoEncontradoException("Associação de material não encontrada");
        }
        return associarMaterial(idPonto,dto);
    }
}
