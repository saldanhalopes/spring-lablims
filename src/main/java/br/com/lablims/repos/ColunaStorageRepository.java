package br.com.lablims.repos;

import br.com.lablims.domain.ColunaStorage;
import br.com.lablims.domain.ColunaStorageTipo;
import br.com.lablims.domain.Setor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ColunaStorageRepository extends JpaRepository<ColunaStorage, Integer> {

    Page<ColunaStorage> findAllById(Integer id, Pageable pageable);

    ColunaStorage findFirstBySetor(Setor setor);

    ColunaStorage findFirstByTipo(ColunaStorageTipo colunaStorageTipo);

}
