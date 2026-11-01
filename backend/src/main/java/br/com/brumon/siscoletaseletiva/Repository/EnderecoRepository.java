package br.com.brumon.siscoletaseletiva.Repository;

import br.com.brumon.siscoletaseletiva.Entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {
    List<Endereco> findByMorador_id(Integer id);
    List<Endereco> findByUfContainingIgnoreCase(String uf);
    List<Endereco> findByMunicipioContainingIgnoreCase(String municipio);
    List<Endereco> findByCep(String cep);
    List<Endereco> findByLogradouroContainingIgnoreCase(String logradouro);
    List<Endereco> findByBairroContainingIgnoreCase(String bairro);
    void deleteByMorador_id(Integer id);
}
