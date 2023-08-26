package br.com.lablims.service;

import br.com.lablims.domain.Usuario;
import br.com.lablims.model.RegistrationRequest;
import br.com.lablims.repos.GrupoRepository;
import br.com.lablims.repos.UsuarioRepository;
import br.com.lablims.util.UserRoles;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class RegistrationService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final GrupoRepository grupoRepository;

    public RegistrationService(final UsuarioRepository usuarioRepository,
            final PasswordEncoder passwordEncoder, final GrupoRepository grupoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.grupoRepository = grupoRepository;
    }

    public boolean usernameExists(final RegistrationRequest registrationRequest) {
        return usuarioRepository.existsByUsernameIgnoreCase(registrationRequest.getUsername());
    }

    public void register(final RegistrationRequest registrationRequest) {
        log.info("registering new user: {}", registrationRequest.getUsername());
        final Usuario usuario = new Usuario();
        usuario.setEmail(registrationRequest.getEmail());
        usuario.setNome(registrationRequest.getNome());
        usuario.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        usuario.setSobrenome(registrationRequest.getSobrenome());
        usuario.setUsername(registrationRequest.getUsername());
        // assign default role
        usuario.setGrupos(Collections.singleton(grupoRepository.findTopByRegra(UserRoles.ADMIN)));
        usuarioRepository.save(usuario);
    }

}
