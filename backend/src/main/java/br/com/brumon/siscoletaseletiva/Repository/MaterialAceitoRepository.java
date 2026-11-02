package br.com.brumon.siscoletaseletiva.Repository;

import br.com.brumon.siscoletaseletiva.Entity.MaterialAceito;
import br.com.brumon.siscoletaseletiva.Entity.MaterialAceitoId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaterialAceitoRepository extends JpaRepository<MaterialAceito, MaterialAceitoId> {
    boolean existsByPontoColeta_IdAndMaterial_Id(Integer idPonto, Integer idMaterial);
    Page<MaterialAceito> findByPontoColeta_Id(Integer idPonto, Pageable pageable);
    Optional<MaterialAceito> findByPontoColeta_IdAndMaterial_Id(Integer idPonto, Integer idMaterial);
    boolean existsByPontoColeta_Id(Integer idPonto);
    boolean existsByMaterial_Id(Integer idMaterial);
}
