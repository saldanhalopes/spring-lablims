package br.com.lablims.repos;

import br.com.lablims.domain.SolucaoParemetro;
import br.com.lablims.domain.SolucaoRegistro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SolucaoParemetroRepository extends JpaRepository<SolucaoParemetro, Integer> {

    Page<SolucaoParemetro> findAllById(Integer id, Pageable pageable);

    SolucaoParemetro findFirstBySolucaoRegistro(SolucaoRegistro solucaoRegistro);

}
