package br.com.brumon.siscoletaseletiva.repository;
import br.com.brumon.siscoletaseletiva.model.entity.PontoColeta;
import br.com.brumon.siscoletaseletiva.model.enums.SituacaoPonto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Usando page em vez de lista pq o requisito no PDF pede que as consultas
// aos pontos usem filtros e paginação, os filtros são
// esses normais que fazemos no repository, mas a paginação
// é feita com o a classe Page e Pageable, que estão aí
// abaixo. Dessa forma, em vez do sistema retornar, por exemplo,
// 200 pontos (muitos), será possível retornar apenas 10, 20, 50, etc, por página,
// e o usuário poderá navegar entre as páginas, como em uma pesquisa no Google, por exemplo

@Repository
public interface PontoColetaRepository extends JpaRepository<PontoColeta, Integer> {
    Page<PontoColeta> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
    Page<PontoColeta> findByCep(String cep, Pageable pageable);
    Page<PontoColeta> findByMunicipioIgnoreCase(String municipio, Pageable pageable);
    Page<PontoColeta> findByUf(String uf, Pageable pageable);
    Page<PontoColeta> findByBairro(String bairro, Pageable pageable);
    Page<PontoColeta> findBySituacao(SituacaoPonto situacao, Pageable pageable);
    Page<PontoColeta> findByRecolhimentoDomiciliar(Boolean recolhimentoDomiciliar, Pageable pageable);
}
