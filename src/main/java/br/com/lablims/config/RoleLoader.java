package br.com.lablims.config;

import br.com.lablims.domain.Grupo;
import br.com.lablims.repos.GrupoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class RoleLoader implements ApplicationRunner {

    private final GrupoRepository grupoRepository;

    public RoleLoader(final GrupoRepository grupoRepository) {
        this.grupoRepository = grupoRepository;
    }

    @Override
    public void run(final ApplicationArguments args) {
        if (grupoRepository.count() != 0) {
            return;
        }
        log.info("initializing roles");
        final Grupo adminGrupo = new Grupo();
        adminGrupo.setRegra("ADMIN");
        grupoRepository.save(adminGrupo);
    }

}
