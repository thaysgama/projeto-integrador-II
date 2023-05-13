package com.br.dhfinancial.keycloak.users.model;

public record User(
        String id,
        String name,
        String surname,
        String CPF,
        String phoneNumber,
        String username,
        String password,
        String email
) {

    public User withUUID(String UUID) {
        return new User(
                UUID,
                name(),
                surname(),
                CPF(),
                phoneNumber(),
                username(),
                password(),
                email()
        );
    }
}
