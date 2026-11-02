package br.com.brumon.siscoletaseletiva.Service;

import br.com.brumon.siscoletaseletiva.DTO.Request.AlterarSituacaoPontoRequestDTO;
import br.com.brumon.siscoletaseletiva.DTO.Request.PontoColetaRequestDTO;
import br.com.brumon.siscoletaseletiva.DTO.Response.PontoColetaResponseDTO;
import br.com.brumon.siscoletaseletiva.Entity.PontoColeta;
import br.com.brumon.siscoletaseletiva.Enum.SituacaoPonto;
import br.com.brumon.siscoletaseletiva.Exception.RecursoNaoEncontradoException;
import br.com.brumon.siscoletaseletiva.Repository.PontoColetaRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PontoColetaService {
    private final PontoColetaRepository rep;
    public PontoColetaService (PontoColetaRepository rep) {
        this.rep = rep;
    }

    public Page<PontoColetaResponseDTO> findAll(Pageable pageable) {
        Page<PontoColeta> pontos = rep.findAll(pageable);
        if (pontos.isEmpty()) {
            throw new RecursoNaoEncontradoException("Não há pontos de coleta cadastrados");
        }
        return pontos.map(pontoColeta -> new PontoColetaResponseDTO(
                        pontoColeta.getId(),
                        pontoColeta.getNome(),
                        pontoColeta.getCep(),
                        pontoColeta.getMunicipio(),
                        pontoColeta.getUf(),
                        pontoColeta.getBairro(),
                        pontoColeta.getLogradouro(),
                        pontoColeta.getNumero(),
                        pontoColeta.getComplemento(),
                        pontoColeta.getContato(),
                        pontoColeta.getHorarios(),
                        pontoColeta.getSituacao(),
                        pontoColeta.getRecolhimentoDomiciliar()
                ));
    }
    public PontoColetaResponseDTO findById(Integer id) {
        PontoColeta ponto = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Ponto de coleta não encontrado"));
        return new PontoColetaResponseDTO(
                ponto.getId(),
                ponto.getNome(),
                ponto.getCep(),
                ponto.getMunicipio(),
                ponto.getUf(),
                ponto.getBairro(),
                ponto.getLogradouro(),
                ponto.getNumero(),
                ponto.getComplemento(),
                ponto.getContato(),
                ponto.getHorarios(),
                ponto.getSituacao(),
                ponto.getRecolhimentoDomiciliar()
        );
    }
    public Page<PontoColetaResponseDTO> findByNome(String nome, Pageable pageable) {
        Page<PontoColeta> pontos = rep.findByNomeContainingIgnoreCase(nome, pageable);
        if (pontos.isEmpty()) {
            throw new RecursoNaoEncontradoException("Ponto de coleta não encontrado");
        }
        return pontos.map(pontoColeta -> new PontoColetaResponseDTO(
                pontoColeta.getId(),
                pontoColeta.getNome(),
                pontoColeta.getCep(),
                pontoColeta.getMunicipio(),
                pontoColeta.getUf(),
                pontoColeta.getBairro(),
                pontoColeta.getLogradouro(),
                pontoColeta.getNumero(),
                pontoColeta.getComplemento(),
                pontoColeta.getContato(),
                pontoColeta.getHorarios(),
                pontoColeta.getSituacao(),
                pontoColeta.getRecolhimentoDomiciliar()
        ));
    }
    public Page<PontoColetaResponseDTO> findByCep(String cep, Pageable pageable) {
        Page<PontoColeta> pontos = rep.findByCep(cep, pageable);
        if (pontos.isEmpty()) {
            throw new RecursoNaoEncontradoException("Ponto de coleta não encontrado");
        }
        return pontos.map(pontoColeta -> new PontoColetaResponseDTO(
                pontoColeta.getId(),
                pontoColeta.getNome(),
                pontoColeta.getCep(),
                pontoColeta.getMunicipio(),
                pontoColeta.getUf(),
                pontoColeta.getBairro(),
                pontoColeta.getLogradouro(),
                pontoColeta.getNumero(),
                pontoColeta.getComplemento(),
                pontoColeta.getContato(),
                pontoColeta.getHorarios(),
                pontoColeta.getSituacao(),
                pontoColeta.getRecolhimentoDomiciliar()
        ));
    }
    public Page<PontoColetaResponseDTO> findByMunicipio(String municipio, Pageable pageable) {
        Page<PontoColeta> pontos = rep.findByMunicipioIgnoreCase(municipio, pageable);
        if (pontos.isEmpty()) {
            throw new RecursoNaoEncontradoException("Ponto de coleta não encontrado");
        }
        return pontos.map(pontoColeta -> new PontoColetaResponseDTO(
                pontoColeta.getId(),
                pontoColeta.getNome(),
                pontoColeta.getCep(),
                pontoColeta.getMunicipio(),
                pontoColeta.getUf(),
                pontoColeta.getBairro(),
                pontoColeta.getLogradouro(),
                pontoColeta.getNumero(),
                pontoColeta.getComplemento(),
                pontoColeta.getContato(),
                pontoColeta.getHorarios(),
                pontoColeta.getSituacao(),
                pontoColeta.getRecolhimentoDomiciliar()
        ));
    }
    public Page<PontoColetaResponseDTO> findByUf(String uf, Pageable pageable) {
        Page<PontoColeta> pontos = rep.findByUf(uf.toUpperCase(), pageable);
        if (pontos.isEmpty()) {
            throw new RecursoNaoEncontradoException("Ponto de coleta não encontrado");
        }
        return pontos.map(pontoColeta -> new PontoColetaResponseDTO(
                pontoColeta.getId(),
                pontoColeta.getNome(),
                pontoColeta.getCep(),
                pontoColeta.getMunicipio(),
                pontoColeta.getUf(),
                pontoColeta.getBairro(),
                pontoColeta.getLogradouro(),
                pontoColeta.getNumero(),
                pontoColeta.getComplemento(),
                pontoColeta.getContato(),
                pontoColeta.getHorarios(),
                pontoColeta.getSituacao(),
                pontoColeta.getRecolhimentoDomiciliar()
        ));
    }
    public Page<PontoColetaResponseDTO> findByBairro(String bairro, Pageable pageable) {
        Page<PontoColeta> pontos = rep.findByBairro(bairro, pageable);
        if (pontos.isEmpty()) {
            throw new RecursoNaoEncontradoException("Ponto de coleta não encontrado");
        }
        return pontos.map(pontoColeta -> new PontoColetaResponseDTO(
                pontoColeta.getId(),
                pontoColeta.getNome(),
                pontoColeta.getCep(),
                pontoColeta.getMunicipio(),
                pontoColeta.getUf(),
                pontoColeta.getBairro(),
                pontoColeta.getLogradouro(),
                pontoColeta.getNumero(),
                pontoColeta.getComplemento(),
                pontoColeta.getContato(),
                pontoColeta.getHorarios(),
                pontoColeta.getSituacao(),
                pontoColeta.getRecolhimentoDomiciliar()
        ));
    }
    public Page<PontoColetaResponseDTO> findBySituacao(SituacaoPonto situacaodto, Pageable pageable) {
        Page<PontoColeta> pontos = rep.findBySituacao(situacaodto, pageable);
        if (pontos.isEmpty()) {
            throw new RecursoNaoEncontradoException("Ponto de coleta não encontrado");
        }
        return pontos.map(pontoColeta -> new PontoColetaResponseDTO(
                pontoColeta.getId(),
                pontoColeta.getNome(),
                pontoColeta.getCep(),
                pontoColeta.getMunicipio(),
                pontoColeta.getUf(),
                pontoColeta.getBairro(),
                pontoColeta.getLogradouro(),
                pontoColeta.getNumero(),
                pontoColeta.getComplemento(),
                pontoColeta.getContato(),
                pontoColeta.getHorarios(),
                pontoColeta.getSituacao(),
                pontoColeta.getRecolhimentoDomiciliar()
        ));
    }
    public Page<PontoColetaResponseDTO> findByRecolhimentoDomiciliar(Boolean recolhimentoDomiciliar, Pageable pageable) {
        Page<PontoColeta> pontos = rep.findByRecolhimentoDomiciliar(recolhimentoDomiciliar, pageable);
        if (pontos.isEmpty()) {
            throw new RecursoNaoEncontradoException("Ponto de coleta não encontrado");
        }
        return pontos.map(pontoColeta -> new PontoColetaResponseDTO(
                pontoColeta.getId(),
                pontoColeta.getNome(),
                pontoColeta.getCep(),
                pontoColeta.getMunicipio(),
                pontoColeta.getUf(),
                pontoColeta.getBairro(),
                pontoColeta.getLogradouro(),
                pontoColeta.getNumero(),
                pontoColeta.getComplemento(),
                pontoColeta.getContato(),
                pontoColeta.getHorarios(),
                pontoColeta.getSituacao(),
                pontoColeta.getRecolhimentoDomiciliar()
        ));
    }
    @Transactional
    public PontoColetaResponseDTO salvarPontoColeta(PontoColetaRequestDTO pontodto) {
        PontoColeta ponto = rep.save(PontoColeta.builder()
                .nome(pontodto.nome())
                .cep(pontodto.cep())
                .municipio(pontodto.municipio())
                .uf(pontodto.uf().toUpperCase())
                .bairro(pontodto.bairro())
                .logradouro(pontodto.logradouro())
                .numero(pontodto.numero())
                .complemento(pontodto.complemento())
                .contato(pontodto.contato())
                .horarios(pontodto.horarios())
                .situacao(SituacaoPonto.ATIVO)
                .recolhimentoDomiciliar(pontodto.recolhimentoDomiciliar())
                .build());
        return new PontoColetaResponseDTO(
                ponto.getId(),
                ponto.getNome(),
                ponto.getCep(),
                ponto.getMunicipio(),
                ponto.getUf(),
                ponto.getBairro(),
                ponto.getLogradouro(),
                ponto.getNumero(),
                ponto.getComplemento(),
                ponto.getContato(),
                ponto.getHorarios(),
                ponto.getSituacao(),
                ponto.getRecolhimentoDomiciliar()
        );
    }
    @Transactional
    public PontoColetaResponseDTO atualizarPontoColeta(Integer id, PontoColetaRequestDTO pontodto){
        PontoColeta ponto = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Ponto de coleta não encontrado"));
        ponto.setNome(pontodto.nome());
        ponto.setCep(pontodto.cep());
        ponto.setMunicipio(pontodto.municipio());
        ponto.setUf(pontodto.uf().toUpperCase());
        ponto.setBairro(pontodto.bairro());
        ponto.setLogradouro(pontodto.logradouro());
        ponto.setNumero(pontodto.numero());
        ponto.setComplemento(pontodto.complemento());
        ponto.setContato(pontodto.contato());
        ponto.setHorarios(pontodto.horarios());
        ponto.setRecolhimentoDomiciliar(pontodto.recolhimentoDomiciliar());
        PontoColeta pontoSalvo = rep.save(ponto);
        return new PontoColetaResponseDTO(
                pontoSalvo.getId(),
                pontoSalvo.getNome(),
                pontoSalvo.getCep(),
                pontoSalvo.getMunicipio(),
                pontoSalvo.getUf(),
                pontoSalvo.getBairro(),
                pontoSalvo.getLogradouro(),
                pontoSalvo.getNumero(),
                pontoSalvo.getComplemento(),
                pontoSalvo.getContato(),
                pontoSalvo.getHorarios(),
                pontoSalvo.getSituacao(),
                pontoSalvo.getRecolhimentoDomiciliar()
        );
    }
    @Transactional
    public PontoColetaResponseDTO alterarSituacao(Integer id, AlterarSituacaoPontoRequestDTO situacaodto) {
        PontoColeta ponto = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Ponto de coleta não encontrado"));
        ponto.setSituacao(situacaodto.situacao());
        PontoColeta pontoSalvo = rep.save(ponto);
        return new PontoColetaResponseDTO(
                pontoSalvo.getId(),
                pontoSalvo.getNome(),
                pontoSalvo.getCep(),
                pontoSalvo.getMunicipio(),
                pontoSalvo.getUf(),
                pontoSalvo.getBairro(),
                pontoSalvo.getLogradouro(),
                pontoSalvo.getNumero(),
                pontoSalvo.getComplemento(),
                pontoSalvo.getContato(),
                pontoSalvo.getHorarios(),
                pontoSalvo.getSituacao(),
                pontoSalvo.getRecolhimentoDomiciliar()
        );
    }

    public void deleteById(Integer id) {
        if (rep.existsById(id)) {
            rep.deleteById(id);
        } else {
            throw new RecursoNaoEncontradoException("Ponto de coleta não encontrado");
        }
    }
}
