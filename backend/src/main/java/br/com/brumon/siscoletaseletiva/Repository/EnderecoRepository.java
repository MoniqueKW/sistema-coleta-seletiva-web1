package br.com.brumon.siscoletaseletiva.Repository;

import br.com.brumon.siscoletaseletiva.Entity.Endereco;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {
    Page<Endereco> findByMorador_Id(Integer idMorador, Pageable pageable);
    Page<Endereco> findByUfContainingIgnoreCase(String uf, Pageable pageable);
    Page<Endereco> findByMunicipioContainingIgnoreCase(String municipio, Pageable pageable);
    Page<Endereco> findByCep(String cep, Pageable pageable);
    Page<Endereco> findByLogradouroContainingIgnoreCase(String logradouro, Pageable pageable);
    Page<Endereco> findByBairroContainingIgnoreCase(String bairro, Pageable pageable);
    void deleteByMorador_Id(Integer idMorador);
    boolean existsByMorador_Id(Integer idMorador);
}
