package com.br.dhfinancial.keycloak.users.dto.response;

public record UserResponse(
        String id,
        String name,
        String surname,
        String CPF,
        String phoneNumber,
        String username,
        String email
) {
}
