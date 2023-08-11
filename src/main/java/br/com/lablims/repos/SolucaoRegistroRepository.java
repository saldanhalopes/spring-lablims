package br.com.lablims.repos;

import br.com.lablims.domain.SolucaoRegistro;
import br.com.lablims.domain.SolucaoTipo;
import br.com.lablims.domain.UnidadeMedida;
import br.com.lablims.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SolucaoRegistroRepository extends JpaRepository<SolucaoRegistro, Integer> {

    Page<SolucaoRegistro> findAllById(Integer id, Pageable pageable);

    SolucaoRegistro findFirstByCriador(Usuario usuario);

    SolucaoRegistro findFirstByConferente(Usuario usuario);

    SolucaoRegistro findFirstByUnidade(UnidadeMedida unidadeMedida);

    SolucaoRegistro findFirstBySolucaoTipo(SolucaoTipo solucaoTipo);

}
