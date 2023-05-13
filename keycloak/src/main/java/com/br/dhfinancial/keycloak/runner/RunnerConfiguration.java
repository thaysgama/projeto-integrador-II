package com.br.dhfinancial.keycloak.runner;

import com.br.dhfinancial.keycloak.properties.KeycloakProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class RunnerConfiguration {

    @Bean
    @ConfigurationProperties(value = "keycloak")
    KeycloakProperties getKeycloakProperties() {
        return new KeycloakProperties();
    }


    @Bean
    Keycloak keycloakAdmin(KeycloakProperties properties) {
        return KeycloakBuilder.builder()
                .serverUrl(properties.getHost())
                .realm("master")
                .username("admin")
                .password("admin")
                .clientId("admin-cli")
                .build();
    }
}

