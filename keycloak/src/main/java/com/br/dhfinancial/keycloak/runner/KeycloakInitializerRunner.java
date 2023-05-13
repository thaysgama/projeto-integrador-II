package com.br.dhfinancial.keycloak.runner;

import com.br.dhfinancial.keycloak.properties.KeycloakProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Slf4j
@Component
@Profile("local")
@RequiredArgsConstructor
public class KeycloakInitializerRunner implements CommandLineRunner {
    private static final List<String> USERS_LIST = List.of("thays.gama", "gustavo.barretto");
    private static final String PASSWORD = "123456";
    private final KeycloakProperties properties;
    private final Keycloak adminClient;


    @Override
    public void run(String... args) {
        log.info("Initializing '{}' realm in Keycloak", properties.getRealm());

        removeRealmIfExists();

        final var realmRepresentation = setupRealmRepresentation();
        final var clientRepresentation = setupClientRepresentation();
        realmRepresentation.setClients(List.of(clientRepresentation));

        List<UserRepresentation> userRepresentations = USERS_LIST.stream()
                .map(this::setupUserRepresentation)
                .toList();

        realmRepresentation.setUsers(userRepresentations);
        adminClient.realms().create(realmRepresentation);
        verifyUsers();
        log.info("'{}' initialization completed successfully!", properties.getRealm());
    }

    private void verifyUsers() {
        String username = USERS_LIST.get(0);
        final var dhFinancialClients = getDhFinancialClients(username);

        final var tokenManager = dhFinancialClients.tokenManager();
        final var accessTokenResponse = tokenManager.grantToken();

        log.info("Testing getting token for '{}'", username);
        final var token = accessTokenResponse.getToken();

        final var tokenSubstring = token.substring(0, 30);
        log.info("'{}' token: {}", username, tokenSubstring);
    }

    private Keycloak getDhFinancialClients(String username) {
        return KeycloakBuilder
                .builder()
                .username(username)
                .password(PASSWORD)
                .realm(properties.getRealm())
                .serverUrl(properties.getHost())
                .clientId(properties.getClientId())
                .clientSecret(properties.getClientSecret())
                .build();
    }

    private UserRepresentation setupUserRepresentation(String user) {
        final var userRepresentation = new UserRepresentation();
        userRepresentation.setCredentials(List.of(getDefaultCredentials()));
        userRepresentation.setUsername(user);
        userRepresentation.setEnabled(true);
        return userRepresentation;
    }

    private CredentialRepresentation getDefaultCredentials() {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(PASSWORD);
        return credentialRepresentation;
    }


    private ClientRepresentation setupClientRepresentation() {
        ClientRepresentation clientRepresentation = new ClientRepresentation();
        clientRepresentation.setClientId(properties.getClientId());
        clientRepresentation.setDirectAccessGrantsEnabled(true);
        clientRepresentation.setPublicClient(false);
        clientRepresentation.setServiceAccountsEnabled(true);
        clientRepresentation.setSecret(properties.getClientSecret());
        clientRepresentation.setRedirectUris(List.of(properties.getRedirectUri()));
        return clientRepresentation;
    }

    private RealmRepresentation setupRealmRepresentation() {
        final var realmRepresentation = new RealmRepresentation();
        realmRepresentation.setRealm(properties.getRealm());
        realmRepresentation.setEnabled(true);
        realmRepresentation.setRegistrationAllowed(true);
        return realmRepresentation;
    }

    private void removeRealmIfExists() {
        final var realmsResource = adminClient.realms();
        final var realms = realmsResource.findAll();

        final var representationOptional = realms.stream()
                .filter(filterByName(properties.getRealm()))
                .findAny();

        representationOptional.ifPresent(action -> {
            log.info("Trying to remove pre-configured realm '{}'.", action.getRealm());
            adminClient.realm(action.getRealm()).remove();
            log.info("Removed pre-configured realm '{}'", action.getRealm());
        });
    }

    private Predicate<RealmRepresentation> filterByName(String name) {
        return realm -> realm.getRealm().equalsIgnoreCase(name);
    }
}
