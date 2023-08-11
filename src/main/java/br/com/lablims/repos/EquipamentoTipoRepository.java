package br.com.lablims.repos;

import br.com.lablims.domain.EquipamentoTipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EquipamentoTipoRepository extends JpaRepository<EquipamentoTipo, Integer> {

    Page<EquipamentoTipo> findAllById(Integer id, Pageable pageable);

}
