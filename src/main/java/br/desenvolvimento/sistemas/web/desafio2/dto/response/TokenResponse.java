package br.desenvolvimento.sistemas.web.desafio2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponse {

    private String token;
    private String type;
    private long expiresIn;
    private String username;
    private String perfil;
}

