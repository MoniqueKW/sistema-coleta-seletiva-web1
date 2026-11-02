package br.com.brumon.siscoletaseletiva.Repository;

import br.com.brumon.siscoletaseletiva.Entity.Morador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MoradorRepository extends JpaRepository<Morador, Integer> {
    List<Morador> findByNomeContainingIgnoreCase(String nome);
    Optional<Morador> findByEmail(String email);
    Optional<Morador> findByTelefone(String telefone);
    boolean existsByEmail(String email);
}
