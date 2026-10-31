package br.com.brumon.siscoletaseletiva.Repository;

import br.com.brumon.siscoletaseletiva.Entity.PontoColeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PontoColetaRepository extends JpaRepository<PontoColeta, Integer> {
    Optional<PontoColeta> findByNome(String nome);
    List<PontoColeta> findByCep(String cep);
    List<PontoColeta> findByMunicipio(String municipio);
    List<PontoColeta> findByUf(String uf);
    List<PontoColeta> findByBairro(String bairro);
    List<PontoColeta> findBySituacao(String situacao);
    List<PontoColeta> findByRecolhimentoDomiciliar(Boolean recolhimentoDomiciliar);
    void deleteByNome(String nome);
}
