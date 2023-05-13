package com.br.dhfinancial.keycloak.auth.exception;

public class AttemptLoginException extends RuntimeException {
    public AttemptLoginException(String message) {
        super(message);
    }
}
