package br.com.brumon.siscoletaseletiva.Repository;

import br.com.brumon.siscoletaseletiva.Entity.Solicitacao;
import br.com.brumon.siscoletaseletiva.Enum.SituacaoSolicitacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Integer> {
    Page<Solicitacao> findByPontoColetaIdAndSituacaoAndDataCriacaoBetween(
            Integer idPonto,
            SituacaoSolicitacao situacao,
            LocalDateTime dataInicio,
            LocalDateTime dataFim,
            Pageable pageable
    );

    Page<Solicitacao> findByPontoColeta_Id(Integer idPonto, Pageable pageable);
    Page<Solicitacao> findBySituacao(SituacaoSolicitacao situacao, Pageable pageable);
    Page<Solicitacao> findByMoradorId(Integer idMorador, Pageable pageable);
}