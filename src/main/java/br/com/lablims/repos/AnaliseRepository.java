package br.com.lablims.repos;

import br.com.lablims.domain.Analise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AnaliseRepository extends JpaRepository<Analise, Integer> {

    Page<Analise> findAllById(Integer id, Pageable pageable);

}
