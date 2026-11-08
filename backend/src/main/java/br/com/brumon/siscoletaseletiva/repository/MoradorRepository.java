package br.com.brumon.siscoletaseletiva.repository;

import br.com.brumon.siscoletaseletiva.model.entity.Morador;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Repository
public interface MoradorRepository extends JpaRepository<Morador, Integer> {
    Page<Morador> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
    Optional<Morador> findByEmail(String email);
    boolean existsByEmail(String email);
}
