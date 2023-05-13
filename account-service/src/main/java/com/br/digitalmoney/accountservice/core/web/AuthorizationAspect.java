package com.br.digitalmoney.accountservice.core.web;

import com.br.digitalmoney.accountservice.core.web.exception.AuthorizationNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static java.util.Objects.isNull;

@Aspect
@Component
public class AuthorizationAspect {

    private final String USER_URL = "http://user-service:8080";

    @Around("@annotation(checkAuthorization)")
    public Object checkAuthorization(
            ProceedingJoinPoint joinPoint,
            CheckAuthorization checkAuthorization
            ) throws Throwable {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes())
                .getRequest();
        String authorizationHeader = request.getHeader("Authorization");

        if (isNull(authorizationHeader) || authorizationHeader.isEmpty()) {
            throw new AuthorizationNotFoundException();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);


        ResponseEntity<String> response = new RestTemplate()
                .exchange(
                        USER_URL + "/api/v1/user/ping",
                        HttpMethod.GET,
                        new HttpEntity<>("body", headers),
                        String.class
                );

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new AuthorizationNotFoundException();
        }

        return joinPoint.proceed();
    }


}
