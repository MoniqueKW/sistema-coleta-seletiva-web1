package br.com.brumon.siscoletaseletiva.Repository;

import br.com.brumon.siscoletaseletiva.Entity.MaterialAceito;
import br.com.brumon.siscoletaseletiva.Entity.MaterialAceitoId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialAceitoRepository extends JpaRepository<MaterialAceito, MaterialAceitoId> {
}
