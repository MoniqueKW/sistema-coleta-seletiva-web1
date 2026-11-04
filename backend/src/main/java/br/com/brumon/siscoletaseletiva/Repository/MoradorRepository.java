package br.com.brumon.siscoletaseletiva.Repository;

import br.com.brumon.siscoletaseletiva.Entity.Morador;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Repository
public interface MoradorRepository extends JpaRepository<Morador, Integer> {
    Page<Morador> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
    Optional<Morador> findByEmail(String email);
    Optional<Morador> findByTelefone(String telefone);
    boolean existsByEmail(String email);
}
