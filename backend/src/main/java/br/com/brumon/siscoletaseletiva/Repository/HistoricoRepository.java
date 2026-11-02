package br.com.brumon.siscoletaseletiva.Repository;

import br.com.brumon.siscoletaseletiva.Entity.Historico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoRepository extends JpaRepository<Historico, Integer> {
    //Método útil para buscar todo o histórico de uma solicitação específica.

    List<Historico> findBySolicitacaoIdOrderByDataAsc(Integer idSolicitacao);
}