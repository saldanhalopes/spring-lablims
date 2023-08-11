package br.com.lablims.repos;

import br.com.lablims.domain.SolucaoTipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SolucaoTipoRepository extends JpaRepository<SolucaoTipo, Integer> {

    Page<SolucaoTipo> findAllById(Integer id, Pageable pageable);

}
