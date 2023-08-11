package br.com.lablims.repos;

import br.com.lablims.domain.SolucaoEquipamento;
import br.com.lablims.domain.SolucaoRegistro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SolucaoEquipamentoRepository extends JpaRepository<SolucaoEquipamento, Integer> {

    Page<SolucaoEquipamento> findAllById(Integer id, Pageable pageable);

    SolucaoEquipamento findFirstBySolucaoRegistro(SolucaoRegistro solucaoRegistro);

}
