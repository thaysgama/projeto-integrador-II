package com.br.dhfinancial.keycloak.auth.dto.response;

public record AuthResponse(
        String access_token,
        String refresh_token,
        String expires_in,
        String refresh_expires_in,
        String token_type
) {
}
