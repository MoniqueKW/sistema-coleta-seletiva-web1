package br.com.brumon.siscoletaseletiva.repository;
import br.com.brumon.siscoletaseletiva.model.entity.PontoColeta;
import br.com.brumon.siscoletaseletiva.model.enums.SituacaoPonto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PontoColetaRepository extends JpaRepository<PontoColeta, Integer>, JpaSpecificationExecutor<PontoColeta> {


}
