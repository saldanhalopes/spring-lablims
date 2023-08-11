package br.com.lablims.repos;

import br.com.lablims.domain.CelulaTipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CelulaTipoRepository extends JpaRepository<CelulaTipo, Integer> {

    Page<CelulaTipo> findAllById(Integer id, Pageable pageable);

}
