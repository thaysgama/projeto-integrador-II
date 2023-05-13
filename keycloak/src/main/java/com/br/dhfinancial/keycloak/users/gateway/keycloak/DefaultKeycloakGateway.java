package com.br.dhfinancial.keycloak.users.gateway.keycloak;

import com.br.dhfinancial.keycloak.users.gateway.KeycloakGateway;
import com.br.dhfinancial.keycloak.users.gateway.exception.KeycloakException;
import com.br.dhfinancial.keycloak.users.mapper.UserMapper;
import com.br.dhfinancial.keycloak.users.model.User;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DefaultKeycloakGateway implements KeycloakGateway {

    private final Keycloak keycloak;
    private final UserMapper userMapper;
    private final String REALM = "dh-financial";


    @Override
    public User create(User u) {
        Response response = keycloak
                .realm(REALM)
                .users()
                .create(userMapper.fromUserToUserRepresentation(u));
        u.withUUID(response.getHeaderString("Location"));
        return u;
    }

    @Override
    public void delete(String id) {
         Response response = keycloak
                 .realm(REALM)
                 .users()
                 .delete(id);
         if(!hasSuccessfullyRequest(response.getStatus())) {
             throw new KeycloakException("error when tried to delete an user");
         }
    }

    @Override
    public User getUserById(String id) {
        final UserResource userResource = getKeycloakUser(id);
        return userMapper.fromUserResourceToUser(userResource);
    }

    @Override
    public List<User> getUsers() {
        return keycloak.realm(REALM)
                .users()
                .list()
                .stream().map(u -> userMapper.fromUserRepresentationToUser(u))
                .collect(Collectors.toList());
    }

    @Override
    public void logout(String id) {
        UserResource user = getKeycloakUser(id);
        user.logout();
    }

    private boolean hasSuccessfullyRequest(int status) {
        return status > 200 && status < 300;
    }

    private UserResource getKeycloakUser(String UUID) {
        try {
            return keycloak
                    .realm(REALM)
                    .users()
                    .get(UUID);

        } catch (Exception e) {
            throw new KeycloakException("error when tried to get an user");
        }
    }

}
