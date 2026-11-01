package br.com.brumon.siscoletaseletiva.Repository;

import br.com.brumon.siscoletaseletiva.Entity.Operador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OperadorRepository extends JpaRepository<Operador, Integer> {
    Optional<Operador> findByNomeContainingIgnoreCase(String nome);
    Optional<Operador> findByEmail(String email);
    List<Operador> findByPontoColeta_id(Integer idPonto);
    void deleteByNome(String nome);
}
