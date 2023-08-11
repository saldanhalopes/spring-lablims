package br.com.lablims.repos;

import br.com.lablims.domain.Turno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TurnoRepository extends JpaRepository<Turno, Integer> {

    Page<Turno> findAllById(Integer id, Pageable pageable);

}
