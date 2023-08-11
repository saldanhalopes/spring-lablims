package br.com.lablims.repos;

import br.com.lablims.domain.Arquivos;
import br.com.lablims.domain.Equipamento;
import br.com.lablims.domain.EquipamentoAtividade;
import br.com.lablims.domain.EquipamentoLog;
import br.com.lablims.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EquipamentoLogRepository extends JpaRepository<EquipamentoLog, Integer> {

    Page<EquipamentoLog> findAllById(Integer id, Pageable pageable);

    EquipamentoLog findFirstByUsuarioInicio(Usuario usuario);

    EquipamentoLog findFirstByUsuarioFim(Usuario usuario);

    EquipamentoLog findFirstByAnexo(Arquivos arquivos);

    EquipamentoLog findFirstByEquipamento(Equipamento equipamento);

    EquipamentoLog findFirstByAtividade(EquipamentoAtividade equipamentoAtividade);

}
