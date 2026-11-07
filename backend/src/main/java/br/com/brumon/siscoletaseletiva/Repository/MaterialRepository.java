package br.com.brumon.siscoletaseletiva.Repository;

import br.com.brumon.siscoletaseletiva.Entity.Material;
import br.com.brumon.siscoletaseletiva.Enum.SituacaoMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {
    Page<Material> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
    Page<Material> findBySituacao(SituacaoMaterial situacao, Pageable pageable);
    boolean existsByNomeIgnoreCase(String nome);
}