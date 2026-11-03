package br.com.brumon.siscoletaseletiva.Repository;

import br.com.brumon.siscoletaseletiva.Entity.Solicitacao;
import br.com.brumon.siscoletaseletiva.Enum.SituacaoSolicitacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Integer> {
    Page<Solicitacao> findByPontoColeta_IdAndSituacaoAndDataCriacaoBetween(
            Integer idPonto,
            SituacaoSolicitacao situacao,
            LocalDateTime dataInicio,
            LocalDateTime dataFim,
            Pageable pageable
    );
    Page<Solicitacao> findByPontoColeta_IdAndDataCriacaoBetween(
            Integer idPonto,
            LocalDateTime dataInicio,
            LocalDateTime dataFim,
            Pageable pageable
    );
    Page<Solicitacao> findByMorador_IdAndSituacaoAndDataCriacaoBetween(
            Integer idMorador,
            SituacaoSolicitacao situacao,
            LocalDateTime dataInicio,
            LocalDateTime dataFim,
            Pageable pageable
    );
    Page<Solicitacao> findByMorador_IdAndDataCriacaoBetween(
            Integer idMorador,
            LocalDateTime dataInicio,
            LocalDateTime dataFim,
            Pageable pageable
    );
    Page<Solicitacao> findByPontoColeta_IdAndMorador_Id(Integer idPonto, Integer idMorador, Pageable pageable);
    Page<Solicitacao> findByPontoColeta_Id(Integer idPonto, Pageable pageable);
    Page<Solicitacao> findBySituacao(SituacaoSolicitacao situacao, Pageable pageable);
    Page<Solicitacao> findByMorador_Id(Integer idMorador, Pageable pageable);
    Page<Solicitacao> findByMorador_IdAndSituacao(Integer idMorador, SituacaoSolicitacao situacao, Pageable pageable);
    Page<Solicitacao> findByPontoColeta_IdAndSituacao(Integer idPonto, SituacaoSolicitacao situacao, Pageable pageable);
    Optional<Solicitacao> findByProtocoloUnico(String protocolo);
    boolean existsByPontoColeta_IdAndSituacaoIn(Integer idPonto, Collection<SituacaoSolicitacao> situacoes);
    boolean existsByMorador_Id(Integer idMorador);
    boolean existsByPontoColeta_Id(Integer idPonto);
    boolean existsByProtocoloUnico(String protocoloUnico);
}