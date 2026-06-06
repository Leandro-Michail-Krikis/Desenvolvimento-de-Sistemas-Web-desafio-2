package br.desenvolvimento.sistemas.web.desafio2.service;

import br.desenvolvimento.sistemas.web.desafio2.dto.request.LoginRequest;
import br.desenvolvimento.sistemas.web.desafio2.dto.request.RegisterRequest;
import br.desenvolvimento.sistemas.web.desafio2.dto.response.TokenResponse;
import br.desenvolvimento.sistemas.web.desafio2.entity.PerfilAcesso;
import br.desenvolvimento.sistemas.web.desafio2.entity.Usuario;
import br.desenvolvimento.sistemas.web.desafio2.repository.UsuarioRepository;
import br.desenvolvimento.sistemas.web.desafio2.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public TokenResponse register(RegisterRequest request) {
        if (usuarioRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username '" + request.getUsername() + "' já está em uso");
        }

        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .perfil(PerfilAcesso.USER)
                .build();

        usuarioRepository.save(usuario);

        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getUsername());
        String token = jwtService.generateToken(userDetails);

        return TokenResponse.builder()
                .token(token)
                .type("Bearer")
                .expiresIn(jwtService.getJwtExpiration())
                .username(usuario.getUsername())
                .perfil(usuario.getPerfil().name())
                .build();
    }

    public TokenResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow();

        String token = jwtService.generateToken(userDetails);

        return TokenResponse.builder()
                .token(token)
                .type("Bearer")
                .expiresIn(jwtService.getJwtExpiration())
                .username(usuario.getUsername())
                .perfil(usuario.getPerfil().name())
                .build();
    }
}

