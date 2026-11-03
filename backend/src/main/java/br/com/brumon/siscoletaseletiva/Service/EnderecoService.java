package br.com.brumon.siscoletaseletiva.Service;

import br.com.brumon.siscoletaseletiva.DTO.Request.EnderecoRequestDTO;
import br.com.brumon.siscoletaseletiva.DTO.Response.EnderecoResponseDTO;
import br.com.brumon.siscoletaseletiva.Entity.Endereco;
import br.com.brumon.siscoletaseletiva.Exception.RecursoNaoEncontradoException;
import br.com.brumon.siscoletaseletiva.Repository.EnderecoRepository;
import br.com.brumon.siscoletaseletiva.Repository.MoradorRepository;
import jakarta.transaction.Transactional;
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

    public Page<EnderecoResponseDTO> findAll(Pageable pageable) {
        Page<Endereco> page = rep.findAll(pageable);
        return page.map(endereco -> new EnderecoResponseDTO(
                endereco.getId(),
                endereco.getMorador().getId(),
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getMunicipio(),
                endereco.getUf(),
                endereco.getCep()
        ));
    }
    public EnderecoResponseDTO findById(Integer id) {
        Endereco endereco = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Endereço não " +
                "encontrado"));
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
    public Page<EnderecoResponseDTO> findByMoradorId(Integer idMorador, Pageable pageable) {
        Page<Endereco> page = rep.findByMorador_Id(idMorador, pageable);
        return page.map(endereco -> new EnderecoResponseDTO(
                endereco.getId(),
                endereco.getMorador().getId(),
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getMunicipio(),
                endereco.getUf(),
                endereco.getCep()
        ));
    }
    public Page<EnderecoResponseDTO> findByCep(String cep, Pageable pageable) {
        Page<Endereco> page = rep.findByCep(cep, pageable);
        return page.map(endereco -> new EnderecoResponseDTO(
                endereco.getId(),
                endereco.getMorador().getId(),
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getMunicipio(),
                endereco.getUf(),
                endereco.getCep()
        ));
    }
    public Page<EnderecoResponseDTO> findByUf(String uf, Pageable pageable) {
        Page<Endereco> page = rep.findByUfContainingIgnoreCase(uf, pageable);
        return page.map(endereco -> new EnderecoResponseDTO(
                endereco.getId(),
                endereco.getMorador().getId(),
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getMunicipio(),
                endereco.getUf(),
                endereco.getCep()
        ));
    }
    public Page<EnderecoResponseDTO> findByMunicipio(String municipio, Pageable pageable) {
        Page<Endereco> page = rep.findByMunicipioContainingIgnoreCase(municipio, pageable);
        return page.map(endereco -> new EnderecoResponseDTO(
                endereco.getId(),
                endereco.getMorador().getId(),
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getMunicipio(),
                endereco.getUf(),
                endereco.getCep()
        ));
    }
    public Page<EnderecoResponseDTO> findByBairro(String bairro, Pageable pageable) {
        Page<Endereco> page = rep.findByBairroContainingIgnoreCase(bairro, pageable);
        return page.map(endereco -> new EnderecoResponseDTO(
                endereco.getId(),
                endereco.getMorador().getId(),
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getMunicipio(),
                endereco.getUf(),
                endereco.getCep()
        ));
    }
    public Page<EnderecoResponseDTO> findByLogradouro(String logradouro, Pageable pageable) {
        Page<Endereco> page = rep.findByLogradouroContainingIgnoreCase(logradouro, pageable);
        return page.map(endereco -> new EnderecoResponseDTO(
                endereco.getId(),
                endereco.getMorador().getId(),
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getMunicipio(),
                endereco.getUf(),
                endereco.getCep()
        ));
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
        return new EnderecoResponseDTO(
                enderecoSalvo.getId(),
                enderecoSalvo.getMorador().getId(),
                enderecoSalvo.getLogradouro(),
                enderecoSalvo.getNumero(),
                enderecoSalvo.getComplemento(),
                enderecoSalvo.getBairro(),
                enderecoSalvo.getMunicipio(),
                enderecoSalvo.getUf(),
                enderecoSalvo.getCep()
        );
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
}
