package br.com.lablims.repos;

import br.com.lablims.domain.AtaTurno;
import br.com.lablims.domain.Equipamento;
import br.com.lablims.domain.Setor;
import br.com.lablims.domain.Turno;
import br.com.lablims.domain.Usuario;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AtaTurnoRepository extends JpaRepository<AtaTurno, Integer> {

    Page<AtaTurno> findAllById(Integer id, Pageable pageable);

    AtaTurno findFirstByUsuario(Usuario usuario);

    AtaTurno findFirstBySetor(Setor setor);

    List<AtaTurno> findAllByEquipamentos(Equipamento equipamento);

    AtaTurno findFirstByEquipamentos(Equipamento equipamento);

    AtaTurno findFirstByTurno(Turno turno);

}
