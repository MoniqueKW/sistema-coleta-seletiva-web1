package br.com.brumon.siscoletaseletiva.Service;

import br.com.brumon.siscoletaseletiva.DTO.Request.EnderecoRequestDTO;
import br.com.brumon.siscoletaseletiva.DTO.Response.EnderecoResponseDTO;
import br.com.brumon.siscoletaseletiva.Entity.Endereco;
import br.com.brumon.siscoletaseletiva.Exception.RecursoNaoEncontradoException;
import br.com.brumon.siscoletaseletiva.Repository.EnderecoRepository;
import br.com.brumon.siscoletaseletiva.Repository.MoradorRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EnderecoService {
    private final EnderecoRepository rep;
    private final MoradorRepository repMorador;
    public EnderecoService(EnderecoRepository rep, MoradorRepository repMorador) {
        this.rep = rep;
        this.repMorador = repMorador;
    }
    @Transactional(readOnly = true)
    public Page<EnderecoResponseDTO> findAll(Pageable pageable) {
        Page<Endereco> enderecos = rep.findAll(pageable);
        return enderecos.map(this::converterParaDTO);
    }
    @Transactional(readOnly = true)
    public EnderecoResponseDTO findById(Integer id) {
        Endereco endereco = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Endereço não " +
                "encontrado"));
        return converterParaDTO(endereco);
    }
    @Transactional(readOnly = true)
    public Page<EnderecoResponseDTO> findByMoradorId(Integer idMorador, Pageable pageable) {
        Page<Endereco> enderecos = rep.findByMorador_Id(idMorador, pageable);
        return enderecos.map(this::converterParaDTO);
    }
    @Transactional(readOnly = true)
    public Page<EnderecoResponseDTO> findByCep(String cep, Pageable pageable) {
        Page<Endereco> enderecos = rep.findByCep(cep, pageable);
        return enderecos.map(this::converterParaDTO);
    }
    @Transactional(readOnly = true)
    public Page<EnderecoResponseDTO> findByUf(String uf, Pageable pageable) {
        Page<Endereco> enderecos = rep.findByUfContainingIgnoreCase(uf, pageable);
        return enderecos.map(this::converterParaDTO);
    }
    @Transactional(readOnly = true)
    public Page<EnderecoResponseDTO> findByMunicipio(String municipio, Pageable pageable) {
        Page<Endereco> enderecos = rep.findByMunicipioContainingIgnoreCase(municipio, pageable);
        return enderecos.map(this::converterParaDTO);
    }
    @Transactional(readOnly = true)
    public Page<EnderecoResponseDTO> findByBairro(String bairro, Pageable pageable) {
        Page<Endereco> enderecos = rep.findByBairroContainingIgnoreCase(bairro, pageable);
        return enderecos.map(this::converterParaDTO);
    }
    @Transactional(readOnly = true)
    public Page<EnderecoResponseDTO> findByLogradouro(String logradouro, Pageable pageable) {
        Page<Endereco> enderecos = rep.findByLogradouroContainingIgnoreCase(logradouro, pageable);
        return enderecos.map(this::converterParaDTO);
    }
    @Transactional
    public EnderecoResponseDTO salvarEndereco(EnderecoRequestDTO enderecodto) {
        Endereco endereco = rep.save(Endereco.builder()
                .morador(repMorador.findById(enderecodto.idMorador()).orElseThrow(() -> new RecursoNaoEncontradoException("Morador não encontrado")))
                .logradouro(enderecodto.logradouro())
                .numero(enderecodto.numero())
                .complemento(enderecodto.complemento())
                .bairro(enderecodto.bairro())
                .municipio(enderecodto.municipio())
                .uf(enderecodto.uf())
                .cep(enderecodto.cep())
                .build());
        return converterParaDTO(endereco);
    }
    @Transactional
    public EnderecoResponseDTO atualizarEndereco(Integer id, EnderecoRequestDTO enderecodto) {
        Endereco endereco = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Endereço não encontrado"));
        endereco.setLogradouro(enderecodto.logradouro());
        endereco.setNumero(enderecodto.numero());
        endereco.setComplemento(enderecodto.complemento());
        endereco.setBairro(enderecodto.bairro());
        endereco.setMunicipio(enderecodto.municipio());
        endereco.setUf(enderecodto.uf());
        endereco.setCep(enderecodto.cep());
        Endereco enderecoSalvo = rep.save(endereco);
        return converterParaDTO(enderecoSalvo);
    }
    @Transactional
    public void deleteByMorador_id(Integer idMorador) {
        if (!repMorador.existsById(idMorador)){
            throw new RecursoNaoEncontradoException("Morador não encontrado");
        }
        if (!rep.existsByMorador_Id(idMorador)) {
            throw new RecursoNaoEncontradoException("Endereco não encontrado para esse morador");
        }
        rep.deleteByMorador_Id(idMorador);
    }

    public void deleteById(Integer id) {
       Endereco endereco = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Endereço não encontrado"));
       rep.delete(endereco);
    }

    public EnderecoResponseDTO converterParaDTO(Endereco endereco) {
        return new EnderecoResponseDTO(
                endereco.getId(),
                endereco.getMorador().getId(),
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getMunicipio(),
                endereco.getUf(),
                endereco.getCep()
        );
    }
}
