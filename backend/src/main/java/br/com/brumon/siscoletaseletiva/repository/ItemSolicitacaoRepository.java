package br.com.brumon.siscoletaseletiva.repository;

import br.com.brumon.siscoletaseletiva.dto.response.QuantidadeRecolhidaDTO;
import br.com.brumon.siscoletaseletiva.model.entity.ItemSolicitacao;
import br.com.brumon.siscoletaseletiva.model.entity.ItemSolicitacaoId;
import br.com.brumon.siscoletaseletiva.model.enums.SituacaoSolicitacao;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemSolicitacaoRepository extends JpaRepository<ItemSolicitacao, ItemSolicitacaoId> {
    boolean existsBySolicitacao_PontoColeta_IdAndSolicitacao_SituacaoInAndMaterial_Id(
            Integer idPonto,
            Collection<SituacaoSolicitacao> situacoes,
            Integer idMaterial
    );
    boolean existsByMaterial_Id(Integer idMaterial);
    List<ItemSolicitacao> findBySolicitacao_Id(Integer idSolicitacao);
    void deleteAllBySolicitacao_Id(Integer idSolicitacao);
    Optional<ItemSolicitacao> findBySolicitacao_IdAndMaterial_Id(Integer id, @NotNull Integer integer);

    //Soma a quantidade EFETIVA (nunca a estimada) das solicitações na situação informada
    //(o service passa CONCLUIDA), com a data de conclusão dentro do período.
    //Agrupa pela unidade copiada no item (RN04), então kg e unidade nunca são somados juntos.
    @Query("""
            select new br.com.brumon.siscoletaseletiva.dto.response.QuantidadeRecolhidaDTO(
                m.id, m.nome, i.copiaUnidade, sum(i.qtdEfetiva))
            from ItemSolicitacao i
            join i.solicitacao s
            join i.material m
            where s.situacao = :situacao
              and s.dataConclusao between :inicio and :fim
            group by m.id, m.nome, i.copiaUnidade
            order by m.nome, i.copiaUnidade
            """)
    List<QuantidadeRecolhidaDTO> somarQuantidadeEfetiva(@Param("situacao") SituacaoSolicitacao situacao,
                                                        @Param("inicio") OffsetDateTime inicio,
                                                        @Param("fim") OffsetDateTime fim);}