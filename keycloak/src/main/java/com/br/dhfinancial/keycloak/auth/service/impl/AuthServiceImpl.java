package com.br.dhfinancial.keycloak.auth.service.impl;

import com.br.dhfinancial.keycloak.auth.dto.response.AuthResponse;
import com.br.dhfinancial.keycloak.auth.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;



@AllArgsConstructor
@NoArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {


    @Value("${spring.security.oauth2.client.registration.oauth2-client-credentials.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.oauth2-client-credentials.client-secret}")
    private String CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.registration.oauth2-client-credentials.authorization-grant-type}")
    private String GRANT_TYPE;
    private final String ISSUER_URL = "http://localhost:8081/auth/realms/dh-financial";


    private RestTemplate restTemplate;

    public ResponseEntity<AuthResponse> login(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", CLIENT_ID);
        map.add("client_secret", CLIENT_SECRET);
        map.add("grant_type", GRANT_TYPE);
        map.add("username", username);
        map.add("password", password);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity("http://localhost:8080/auth/realms/dh-financial/openid-connect/token", httpEntity, AuthResponse.class);
        return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
    }
}
