package br.desenvolvimento.sistemas.web.desafio2.config;

import br.desenvolvimento.sistemas.web.desafio2.entity.PerfilAcesso;
import br.desenvolvimento.sistemas.web.desafio2.entity.Usuario;
import br.desenvolvimento.sistemas.web.desafio2.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            usuarioRepository.save(Usuario.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .perfil(PerfilAcesso.ADMIN)
                    .build());
        }

        if (usuarioRepository.findByUsername("user").isEmpty()) {
            usuarioRepository.save(Usuario.builder()
                    .username("user")
                    .password(passwordEncoder.encode("user123"))
                    .perfil(PerfilAcesso.USER)
                    .build());
        }
    }
}

