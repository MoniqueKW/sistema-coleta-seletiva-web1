package br.com.brumon.siscoletaseletiva.Repository;

import br.com.brumon.siscoletaseletiva.Entity.ItemSolicitacao;
import br.com.brumon.siscoletaseletiva.Entity.ItemSolicitacaoId;
import br.com.brumon.siscoletaseletiva.Enum.SituacaoSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.List;

@Repository
public interface ItemSolicitacaoRepository extends JpaRepository<ItemSolicitacao, ItemSolicitacaoId> {
    boolean existsBySolicitacao_PontoColeta_IdAndSolicitacao_SituacaoInAndMaterial_Id(
            Integer idPonto,
            Collection<SituacaoSolicitacao> situacoes,
            Integer idMaterial
    );
    boolean existsByMaterial_Id(Integer idMaterial);
    List<ItemSolicitacao> findBySolicitacao_Id(Integer idSolicitacao);
}
