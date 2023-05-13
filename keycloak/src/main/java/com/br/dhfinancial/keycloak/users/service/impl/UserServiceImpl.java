package com.br.dhfinancial.keycloak.users.service.impl;

import com.br.dhfinancial.keycloak.users.gateway.KeycloakGateway;
import com.br.dhfinancial.keycloak.users.model.User;
import com.br.dhfinancial.keycloak.users.service.UserService;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final KeycloakGateway keycloakGateway;

    @Override
    public User create(User u) {
        return keycloakGateway.create(u);
    }

    @Override
    public void delete(String id) {
        keycloakGateway.delete(id);
    }

    @Override
    public User getUserById(String id) {
        return keycloakGateway.getUserById(id);
    }

    @Override
    public List<User> getUsers() {
        return keycloakGateway.getUsers();
    }

    @Override
    public void logout(String id) {
        keycloakGateway.logout(id);
    }


}
