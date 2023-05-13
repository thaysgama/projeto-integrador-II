package com.br.dhfinancial.keycloak.users.dto.request;

public record UserRequest(
        String name,
        String surname,
        String CPF,
        String phoneNumber,
        String username,
        String email
) {
}
