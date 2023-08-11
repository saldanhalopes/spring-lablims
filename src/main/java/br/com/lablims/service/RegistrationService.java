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
        usuario.setCep(registrationRequest.getCep());
        usuario.setChangePass(registrationRequest.getChangePass());
        usuario.setCidade(registrationRequest.getCidade());
        usuario.setCpf(registrationRequest.getCpf());
        usuario.setDetalhes(registrationRequest.getDetalhes());
        usuario.setEmail(registrationRequest.getEmail());
        usuario.setEndereco(registrationRequest.getEndereco());
        usuario.setEstado(registrationRequest.getEstado());
        usuario.setFailedAccessCount(registrationRequest.getFailedAccessCount());
        usuario.setLastChangePass(registrationRequest.getLastChangePass());
        usuario.setLastLogin(registrationRequest.getLastLogin());
        usuario.setLastLogout(registrationRequest.getLastLogout());
        usuario.setNome(registrationRequest.getNome());
        usuario.setPais(registrationRequest.getPais());
        usuario.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        usuario.setSobrenome(registrationRequest.getSobrenome());
        usuario.setTelefone(registrationRequest.getTelefone());
        usuario.setUsername(registrationRequest.getUsername());
        // assign default role
        usuario.setGrupos(Collections.singleton(grupoRepository.findTopByRegra(UserRoles.ADMIN)));
        usuarioRepository.save(usuario);
    }

}
