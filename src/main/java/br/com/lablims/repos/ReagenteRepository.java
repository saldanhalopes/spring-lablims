package br.com.lablims.repos;

import br.com.lablims.domain.Arquivos;
import br.com.lablims.domain.Reagente;
import br.com.lablims.domain.UnidadeMedida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReagenteRepository extends JpaRepository<Reagente, Integer> {

    Page<Reagente> findAllById(Integer id, Pageable pageable);

    Reagente findFirstByUnidade(UnidadeMedida unidadeMedida);

    Reagente findFirstByFds(Arquivos arquivos);

}
