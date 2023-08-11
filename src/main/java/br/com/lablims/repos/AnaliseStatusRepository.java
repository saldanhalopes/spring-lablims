package br.com.lablims.repos;

import br.com.lablims.domain.AnaliseProdutividade;
import br.com.lablims.domain.AnaliseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AnaliseStatusRepository extends JpaRepository<AnaliseStatus, Integer> {

    Page<AnaliseStatus> findAllById(Integer id, Pageable pageable);

    AnaliseStatus findFirstByAnaliseProdutividade(AnaliseProdutividade analiseProdutividade);

}
