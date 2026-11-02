package br.com.brumon.siscoletaseletiva.Repository;

import br.com.brumon.siscoletaseletiva.Entity.Solicitacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Integer> {

    //Filtrar por período, situação e ponto com paginação
    Page<Solicitacao> findByPontoColetaIdAndSituacaoAndDataCriacaoBetween(
            Integer idPonto,
            String situacao,
            LocalDateTime dataInicio,
            LocalDateTime dataFim,
            Pageable pageable
    );

    //Filtros para as consulta
    Page<Solicitacao> findByPontoColetaId(Integer idPonto, Pageable pageable);

    Page<Solicitacao> findBySituacao(String situacao, Pageable pageable);

    Page<Solicitacao> findByMoradorId(Integer idMorador, Pageable pageable);
}