package br.com.brumon.siscoletaseletiva.repository;

import br.com.brumon.siscoletaseletiva.model.entity.Solicitacao;
import br.com.brumon.siscoletaseletiva.model.enums.SituacaoSolicitacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Integer>, JpaSpecificationExecutor<Solicitacao> {
    Optional<Solicitacao> findByProtocoloUnico(String protocolo);
    boolean existsByPontoColeta_IdAndSituacaoIn(Integer idPonto, Collection<SituacaoSolicitacao> situacoes);
    boolean existsByMorador_Id(Integer idMorador);
    boolean existsByPontoColeta_Id(Integer idPonto);
    boolean existsByProtocoloUnico(String protocoloUnico);
}