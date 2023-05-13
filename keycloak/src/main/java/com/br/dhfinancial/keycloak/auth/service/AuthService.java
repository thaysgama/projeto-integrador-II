package com.br.dhfinancial.keycloak.auth.service;

import com.br.dhfinancial.keycloak.auth.dto.response.AuthResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<AuthResponse> login(String username, String password);

}
