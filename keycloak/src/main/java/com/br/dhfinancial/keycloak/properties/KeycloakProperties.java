package com.br.dhfinancial.keycloak.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

@Slf4j
@Getter
@Setter
@ToString
public class KeycloakProperties implements InitializingBean {
    private String host;
    private String realm;
    private String clientId;
    private String clientSecret;
    private String redirectUri;

    @Override
    public void afterPropertiesSet() {
        log.debug(toString());
    }

}

