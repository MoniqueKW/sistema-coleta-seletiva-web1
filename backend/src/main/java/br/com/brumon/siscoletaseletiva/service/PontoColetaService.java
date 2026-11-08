package br.com.brumon.siscoletaseletiva.service;

import br.com.brumon.siscoletaseletiva.dto.request.AlterarSituacaoPontoRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.request.PontoColetaRequestDTO;
import br.com.brumon.siscoletaseletiva.dto.response.PontoColetaResponseDTO;
import br.com.brumon.siscoletaseletiva.model.entity.PontoColeta;
import br.com.brumon.siscoletaseletiva.model.enums.SituacaoPonto;
import br.com.brumon.siscoletaseletiva.model.enums.SituacaoSolicitacao;
import br.com.brumon.siscoletaseletiva.exception.RecursoNaoEncontradoException;
import br.com.brumon.siscoletaseletiva.exception.RegraNegocioException;
import br.com.brumon.siscoletaseletiva.repository.MaterialAceitoRepository;
import br.com.brumon.siscoletaseletiva.repository.PontoColetaRepository;
import br.com.brumon.siscoletaseletiva.repository.SolicitacaoRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PontoColetaService {
    private final PontoColetaRepository rep;
    private final SolicitacaoRepository repSolicitacao;
    private final MaterialAceitoRepository repMaterialAceito;
    public PontoColetaService (PontoColetaRepository rep, SolicitacaoRepository repSolicitacao, MaterialAceitoRepository repMaterialAceito) {
        this.rep = rep;
        this.repSolicitacao = repSolicitacao;
        this.repMaterialAceito = repMaterialAceito;
    }
    @Transactional(readOnly = true)
    public Page<PontoColetaResponseDTO> findAll(Pageable pageable) {
        Page<PontoColeta> pontos = rep.findAll(pageable);
        return pontos.map(this::converterParaDTO);
    }
    @Transactional(readOnly = true)
    public PontoColetaResponseDTO findById(Integer id) {
        PontoColeta ponto = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Ponto de coleta não encontrado"));
        return converterParaDTO(ponto);
    }
    @Transactional(readOnly = true)
    public Page<PontoColetaResponseDTO> findByNome(String nome, Pageable pageable) {
        Page<PontoColeta> pontos = rep.findByNomeContainingIgnoreCase(nome, pageable);
        return pontos.map(this::converterParaDTO);
    }
    @Transactional(readOnly = true)
    public Page<PontoColetaResponseDTO> findByCep(String cep, Pageable pageable) {
        Page<PontoColeta> pontos = rep.findByCep(cep, pageable);
        return pontos.map(this::converterParaDTO);
    }
    @Transactional(readOnly = true)
    public Page<PontoColetaResponseDTO> findByMunicipio(String municipio, Pageable pageable) {
        Page<PontoColeta> pontos = rep.findByMunicipioIgnoreCase(municipio, pageable);
        return pontos.map(this::converterParaDTO);
    }
    @Transactional(readOnly = true)
    public Page<PontoColetaResponseDTO> findByUf(String uf, Pageable pageable) {
        Page<PontoColeta> pontos = rep.findByUf(uf.toUpperCase(), pageable);
        return pontos.map(this::converterParaDTO);
    }
    @Transactional(readOnly = true)
    public Page<PontoColetaResponseDTO> findByBairro(String bairro, Pageable pageable) {
        Page<PontoColeta> pontos = rep.findByBairro(bairro, pageable);
        return pontos.map(this::converterParaDTO);
    }
    @Transactional(readOnly = true)
    public Page<PontoColetaResponseDTO> findBySituacao(SituacaoPonto situacaodto, Pageable pageable) {
        Page<PontoColeta> pontos = rep.findBySituacao(situacaodto, pageable);
        return pontos.map(this::converterParaDTO);
    }
    @Transactional(readOnly = true)
    public Page<PontoColetaResponseDTO> findByRecolhimentoDomiciliar(Boolean recolhimentoDomiciliar, Pageable pageable) {
        Page<PontoColeta> pontos = rep.findByRecolhimentoDomiciliar(recolhimentoDomiciliar, pageable);
        return pontos.map(this::converterParaDTO);
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
        return converterParaDTO(ponto);
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
        return converterParaDTO(pontoSalvo);
    }
    @Transactional
    public PontoColetaResponseDTO alterarSituacao(Integer id, AlterarSituacaoPontoRequestDTO situacaodto) {
        PontoColeta ponto = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Ponto de coleta não encontrado"));
        // abaixo, feito para verificar se existem solicitacoes que contenham
        // o id do Ponto passado no parametro e uma das situacoes da lista.
        // se tiver, significa que o ponto de coleta está vinculado a uma
        // solicitacao aberta e não pode ser inativado.
        if (situacaodto.situacao() == SituacaoPonto.INATIVO) {
            boolean isAberta = repSolicitacao.existsByPontoColeta_IdAndSituacaoIn(id, List.of(
                    (SituacaoSolicitacao.AGENDADA),
                    (SituacaoSolicitacao.SOLICITADA),
                    (SituacaoSolicitacao.EM_RECOLHIMENTO)));
            if (isAberta) {
                throw new RegraNegocioException("Não é possível inativar o ponto de coleta, pois existem solicitações abertas vinculadas a ele.");
            }
        }
        ponto.setSituacao(situacaodto.situacao());
        PontoColeta pontoSalvo = rep.save(ponto);
        return converterParaDTO(pontoSalvo);
    }
    //apaga se nao tiver associacoes (ou seja, so pra testar)
    @Transactional
    public void deleteById(Integer idPonto) {
        PontoColeta ponto = rep.findById(idPonto)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Ponto de coleta não encontrado."
                ));
        boolean possuiAssociacoes = repMaterialAceito.existsByPontoColeta_Id(idPonto);
        if (possuiAssociacoes) {
            throw new RegraNegocioException("Não é possível excluir o ponto, pois existem materiais associados a ele.");
        }
        boolean possuiSolicitacoes = repSolicitacao.existsByPontoColeta_Id(idPonto);
        if (possuiSolicitacoes) {
            throw new RegraNegocioException("Não é possível excluir o ponto, pois existem solicitações associadas a ele.");
        }
        rep.delete(ponto);
    }

    public PontoColetaResponseDTO converterParaDTO(PontoColeta ponto) {
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
}
