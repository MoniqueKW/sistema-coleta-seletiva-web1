package br.com.brumon.siscoletaseletiva.Repository;

import br.com.brumon.siscoletaseletiva.Entity.ItemSolicitacao;
import br.com.brumon.siscoletaseletiva.Entity.ItemSolicitacaoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemSolicitacaoRepository extends JpaRepository<ItemSolicitacao, ItemSolicitacaoId> {
}
