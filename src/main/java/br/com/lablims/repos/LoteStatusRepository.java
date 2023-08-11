package br.com.lablims.repos;

import br.com.lablims.domain.AnaliseStatus;
import br.com.lablims.domain.Lote;
import br.com.lablims.domain.LoteStatus;
import br.com.lablims.domain.PlanoAnalise;
import br.com.lablims.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LoteStatusRepository extends JpaRepository<LoteStatus, Integer> {

    Page<LoteStatus> findAllById(Integer id, Pageable pageable);

    LoteStatus findFirstByConferente1(Usuario usuario);

    LoteStatus findFirstByConferente2(Usuario usuario);

    LoteStatus findFirstByAnaliseStatus(AnaliseStatus analiseStatus);

    LoteStatus findFirstByPlanoAnalise(PlanoAnalise planoAnalise);

    LoteStatus findFirstByLote(Lote lote);

}
