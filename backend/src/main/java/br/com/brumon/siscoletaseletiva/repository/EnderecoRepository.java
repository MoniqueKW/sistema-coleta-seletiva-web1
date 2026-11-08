package br.com.brumon.siscoletaseletiva.repository;

import br.com.brumon.siscoletaseletiva.model.entity.Endereco;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Integer>, JpaSpecificationExecutor<Endereco> {
    void deleteByMorador_Id(Integer idMorador);
    boolean existsByMorador_Id(Integer idMorador);
}
