package com.br.dhfinancial.keycloak.configuration;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdminConfig {

    @Bean
    Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl("http://localhost:8080/auth")
                .realm("dh-financial")
                .clientId("dh-financial")
                .clientSecret("hQsGX9jfogh7TROStKjuDvSnqM1lrnuW")
                .grantType("client_credentials")
                .build();
    }
}
