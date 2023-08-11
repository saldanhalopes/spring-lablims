package br.com.lablims.repos;

import br.com.lablims.domain.Celula;
import br.com.lablims.domain.CelulaTipo;
import br.com.lablims.domain.Equipamento;
import br.com.lablims.domain.Usuario;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CelulaRepository extends JpaRepository<Celula, Integer> {

    Page<Celula> findAllById(Integer id, Pageable pageable);

    List<Celula> findAllByUsuario(Usuario usuario);

    Celula findFirstByUsuario(Usuario usuario);

    Celula findFirstByTipo(CelulaTipo celulaTipo);

    List<Celula> findAllByEquipamento(Equipamento equipamento);

    Celula findFirstByEquipamento(Equipamento equipamento);

}
