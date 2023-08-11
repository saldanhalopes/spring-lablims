package br.com.lablims.repos;

import br.com.lablims.domain.EscalaMedida;
import br.com.lablims.domain.UnidadeMedida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UnidadeMedidaRepository extends JpaRepository<UnidadeMedida, Integer> {

    Page<UnidadeMedida> findAllById(Integer id, Pageable pageable);

    UnidadeMedida findFirstByEscalaMedida(EscalaMedida escalaMedida);

}
