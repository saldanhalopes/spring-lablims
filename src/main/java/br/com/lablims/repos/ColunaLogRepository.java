package br.com.lablims.repos;

import br.com.lablims.domain.Analise;
import br.com.lablims.domain.Arquivos;
import br.com.lablims.domain.Campanha;
import br.com.lablims.domain.ColunaLog;
import br.com.lablims.domain.ColunaUtil;
import br.com.lablims.domain.Equipamento;
import br.com.lablims.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ColunaLogRepository extends JpaRepository<ColunaLog, Integer> {

    Page<ColunaLog> findAllById(Integer id, Pageable pageable);

    ColunaLog findFirstByUsuarioInicio(Usuario usuario);

    ColunaLog findFirstByUsuarioFim(Usuario usuario);

    ColunaLog findFirstByAnexo(Arquivos arquivos);

    ColunaLog findFirstByAnalise(Analise analise);

    ColunaLog findFirstByColunaUtil(ColunaUtil colunaUtil);

    ColunaLog findFirstByCampanha(Campanha campanha);

    ColunaLog findFirstByEquipamento(Equipamento equipamento);

}
