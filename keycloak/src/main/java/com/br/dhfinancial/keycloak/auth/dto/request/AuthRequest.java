package com.br.dhfinancial.keycloak.auth.dto.request;

public record AuthRequest(
        String username,
        String password
) {

}
