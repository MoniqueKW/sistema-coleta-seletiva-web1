package br.com.brumon.siscoletaseletiva.Repository;

import br.com.brumon.siscoletaseletiva.Entity.Operador;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OperadorRepository extends JpaRepository<Operador, Integer> {
    Page<Operador> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
    Optional<Operador> findByEmail(String email);
    Page<Operador> findByPontoColeta_Id(Integer idPonto, Pageable pageable);
    boolean existsByEmail(String email);
}
