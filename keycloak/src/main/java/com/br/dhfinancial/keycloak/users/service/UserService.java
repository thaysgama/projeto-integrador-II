package com.br.dhfinancial.keycloak.users.service;

import com.br.dhfinancial.keycloak.users.model.User;

import java.util.List;

public interface UserService {

    User create(User u);
    void delete(String id);
    User getUserById(String id);
    List<User> getUsers();

    void logout(String id);

}
