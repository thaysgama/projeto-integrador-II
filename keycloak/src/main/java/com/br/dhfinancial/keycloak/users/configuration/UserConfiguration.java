package com.br.dhfinancial.keycloak.users.configuration;

import com.br.dhfinancial.keycloak.users.gateway.KeycloakGateway;
import com.br.dhfinancial.keycloak.users.gateway.keycloak.DefaultKeycloakGateway;
import com.br.dhfinancial.keycloak.users.mapper.UserMapper;
import com.br.dhfinancial.keycloak.users.mapper.UserMapperImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfiguration {

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.keycloak.realm}")
    private String REALM;

    @Value("${spring.security.oauth2.client.registration.keycloak.host}")
    private String URL;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String CLIENT_SECRET;

    @Bean
    public KeycloakGateway keycloakGateway() {

        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(URL)
                .realm(REALM)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .grantType("client_credentials")
                .build();

        return new DefaultKeycloakGateway(
                keycloak,
                new UserMapperImpl()
        );
    }

}
