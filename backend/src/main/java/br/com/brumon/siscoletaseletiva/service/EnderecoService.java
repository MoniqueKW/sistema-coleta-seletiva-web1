package br.com.brumon.siscoletaseletiva.service;

import br.com.brumon.siscoletaseletiva.dto.request.EnderecoRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.request.EnderecoUpdateRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.response.EnderecoResponseDTO;
import br.com.brumon.siscoletaseletiva.model.entity.Endereco;
import br.com.brumon.siscoletaseletiva.exception.RecursoNaoEncontradoException;
import br.com.brumon.siscoletaseletiva.repository.EnderecoRepository;
import br.com.brumon.siscoletaseletiva.repository.MoradorRepository;
import br.com.brumon.siscoletaseletiva.specification.EnderecoSpecification;
import org.springframework.data.jpa.domain.Specification;
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
    //Todos os filtros enviados são combinados (AND); os que vierem nulos ou em branco são ignorados.
    @Transactional(readOnly = true)
    public Page<EnderecoResponseDTO> listar(Integer idMorador, String cep, String uf, String municipio,
                                            String bairro, String logradouro, Pageable pageable) {
        Specification<Endereco> filtros = Specification.allOf(
                EnderecoSpecification.comMorador(idMorador),
                EnderecoSpecification.comCep(cep),
                EnderecoSpecification.comUf(uf),
                EnderecoSpecification.comMunicipio(municipio),
                EnderecoSpecification.comBairro(bairro),
                EnderecoSpecification.comLogradouro(logradouro));
        Page<Endereco> enderecos = rep.findAll(filtros, pageable);
        return enderecos.map(this::converterParaDTO);
    }

    @Transactional(readOnly = true)
    public EnderecoResponseDTO findById(Integer id) {
        Endereco endereco = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Endereço não " +
                "encontrado"));
        return converterParaDTO(endereco);
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
    public EnderecoResponseDTO atualizarEndereco(Integer id, EnderecoUpdateRequestDTO enderecodto) {
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
