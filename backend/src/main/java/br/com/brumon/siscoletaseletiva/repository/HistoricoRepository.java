package br.com.brumon.siscoletaseletiva.repository;

import br.com.brumon.siscoletaseletiva.model.entity.Historico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoRepository extends JpaRepository<Historico, Integer> {
    Page<Historico> findBySolicitacao_IdOrderByDataAsc(Integer idSolicitacao, Pageable pageable);
    Page<Historico> findByOperador_IdOrderByDataAsc(Integer idOperador, Pageable pageable);
    Page<Historico> findBySolicitacao_IdAndOperador_IdOrderByDataAsc(
            Integer idSolicitacao, Integer idOperador, Pageable pageable);
    boolean existsByOperador_Id(Integer idOperador);
}