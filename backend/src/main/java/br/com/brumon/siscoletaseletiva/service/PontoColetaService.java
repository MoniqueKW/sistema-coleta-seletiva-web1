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
import br.com.brumon.siscoletaseletiva.specification.PontoColetaSpecification;
import org.springframework.data.jpa.domain.Specification;
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
    public Page<PontoColetaResponseDTO> listar(String nome, String municipio,
                                               String bairro, SituacaoPonto situacao,
                                               Boolean recolhimento, Integer idMaterial,
                                               Pageable pageable) {
        //RF06: filtrar por material retorna somente pontos ativos, a nao ser que o usuario
        //passe a situacao inativa como parametro
        SituacaoPonto situacaoEfetiva = (idMaterial != null && situacao == null) ? SituacaoPonto.ATIVO : situacao;
        Specification<PontoColeta> filtros = Specification.allOf(
                PontoColetaSpecification.comNome(nome),
                PontoColetaSpecification.comMunicipio(municipio),
                PontoColetaSpecification.comBairro(bairro),
                PontoColetaSpecification.comSituacao(situacaoEfetiva),
                PontoColetaSpecification.comRecolhimento(recolhimento),
                PontoColetaSpecification.comMaterialAceito(idMaterial));
        Page<PontoColeta> pontos = rep.findAll(filtros, pageable);
        return pontos.map(this::converterParaDTO);
    }
    @Transactional(readOnly = true)
    public PontoColetaResponseDTO findById(Integer id) {
        PontoColeta ponto = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Ponto de coleta não encontrado"));
        return converterParaDTO(ponto);
    }
    @Transactional
    public PontoColetaResponseDTO salvarPontoColeta(PontoColetaRequestDTO pontodto) {
        String contatoBruto = pontodto.contato();
        String telefoneLimpo = null;
        if (contatoBruto != null && !contatoBruto.trim().isEmpty()) {
            telefoneLimpo = contatoBruto.replaceAll("[()\\s+\\-]", "");
            if (telefoneLimpo.length() == 10 || telefoneLimpo.length() == 11) {
                telefoneLimpo = "55" + telefoneLimpo;
            }
        }
        PontoColeta ponto = rep.save(PontoColeta.builder()
                .nome(pontodto.nome())
                .cep(pontodto.cep())
                .municipio(pontodto.municipio())
                .uf(pontodto.uf().toUpperCase())
                .bairro(pontodto.bairro())
                .logradouro(pontodto.logradouro())
                .numero(pontodto.numero())
                .complemento(pontodto.complemento())
                .contato(telefoneLimpo)
                .horarios(pontodto.horarios())
                .situacao(SituacaoPonto.ATIVO)
                .recolhimentoDomiciliar(pontodto.recolhimentoDomiciliar())
                .build());
        return converterParaDTO(ponto);
    }
    @Transactional
    public PontoColetaResponseDTO atualizarPontoColeta(Integer id, PontoColetaRequestDTO pontodto){
        PontoColeta ponto = rep.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Ponto de coleta não encontrado"));
        String contatoBruto = pontodto.contato();
        String telefoneLimpo = null;
        if (contatoBruto != null && !contatoBruto.trim().isEmpty()) {
            telefoneLimpo = contatoBruto.replaceAll("[()\\s+\\-]", "");
            if (telefoneLimpo.length() == 10 || telefoneLimpo.length() == 11) {
                telefoneLimpo = "55" + telefoneLimpo;
            }
        }
        ponto.setNome(pontodto.nome());
        ponto.setCep(pontodto.cep());
        ponto.setMunicipio(pontodto.municipio());
        ponto.setUf(pontodto.uf().toUpperCase());
        ponto.setBairro(pontodto.bairro());
        ponto.setLogradouro(pontodto.logradouro());
        ponto.setNumero(pontodto.numero());
        ponto.setComplemento(pontodto.complemento());
        ponto.setContato(telefoneLimpo);
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
