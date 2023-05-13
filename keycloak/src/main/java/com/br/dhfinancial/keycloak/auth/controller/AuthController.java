package com.br.dhfinancial.keycloak.auth.controller;

import com.br.dhfinancial.keycloak.auth.dto.request.AuthRequest;
import com.br.dhfinancial.keycloak.auth.dto.response.AuthResponse;
import com.br.dhfinancial.keycloak.auth.service.AuthService;
import com.br.dhfinancial.keycloak.users.gateway.KeycloakGateway;
import com.br.dhfinancial.keycloak.users.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        return authService.login(authRequest.username(), authRequest.password());

    }

    @PostMapping("/logout/{id}")
    ResponseEntity<Void> logout(@PathVariable String id) {
        userService.logout(id);
        return ResponseEntity.ok().build();
    }



}
