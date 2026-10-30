package br.com.brumon.siscoletaseletiva.Repository;

import br.com.brumon.siscoletaseletiva.Entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {
    List<Endereco> findByMorador_id(Integer id);
    List<Endereco> findByUf(String uf);
    List<Endereco> findByMunicipio(String municipio);
    List<Endereco> findByCep(String cep);
    List<Endereco> findByLogradouro(String logradouro);
    List<Endereco> findByBairro(String bairro);
}
