package br.com.lablims.repos;

import br.com.lablims.domain.CategoriaMetodologia;
import br.com.lablims.domain.Metodologia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MetodologiaRepository extends JpaRepository<Metodologia, Integer> {

    Page<Metodologia> findAllById(Integer id, Pageable pageable);

    Metodologia findFirstByCategoriaMetodologia(CategoriaMetodologia categoriaMetodologia);

}
