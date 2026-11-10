package br.com.brumon.siscoletaseletiva.repository;

import br.com.brumon.siscoletaseletiva.dto.response.ResumoSituacaoDTO;
import br.com.brumon.siscoletaseletiva.model.entity.Solicitacao;
import br.com.brumon.siscoletaseletiva.model.enums.SituacaoSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Integer>, JpaSpecificationExecutor<Solicitacao> {
    Optional<Solicitacao> findByProtocoloUnico(String protocolo);
    boolean existsByPontoColeta_IdAndSituacaoIn(Integer idPonto, Collection<SituacaoSolicitacao> situacoes);
    boolean existsByMorador_Id(Integer idMorador);
    boolean existsByPontoColeta_Id(Integer idPonto);
    boolean existsByProtocoloUnico(String protocoloUnico);

    //Conta as solicitações criadas no período, agrupadas por situação.
    //Situações sem nenhuma solicitação não vêm na consulta (o service completa com zero).
    @Query("""
            select new br.com.brumon.siscoletaseletiva.dto.response.ResumoSituacaoDTO(s.situacao, count(s))
            from Solicitacao s
            where s.dataCriacao between :inicio and :fim
            group by s.situacao
            """)
    List<ResumoSituacaoDTO> contarPorSituacao(@Param("inicio") OffsetDateTime inicio,
                                              @Param("fim") OffsetDateTime fim);}