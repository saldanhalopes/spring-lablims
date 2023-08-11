package br.com.lablims.repos;

import br.com.lablims.domain.PlanoAnaliseReagente;
import br.com.lablims.domain.Reagente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PlanoAnaliseReagenteRepository extends JpaRepository<PlanoAnaliseReagente, Integer> {

    Page<PlanoAnaliseReagente> findAllById(Integer id, Pageable pageable);

    PlanoAnaliseReagente findFirstByReagente(Reagente reagente);

}
