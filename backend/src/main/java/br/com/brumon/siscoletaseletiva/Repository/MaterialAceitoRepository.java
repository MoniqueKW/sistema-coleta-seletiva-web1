package br.com.brumon.siscoletaseletiva.Repository;

import br.com.brumon.siscoletaseletiva.Entity.MaterialAceito;
import br.com.brumon.siscoletaseletiva.Entity.MaterialAceitoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialAceitoRepository extends JpaRepository<MaterialAceito, MaterialAceitoId> {
}
