package com.br.digitalmoney.accountservice.core.web.exception;

public class AuthorizationNotFoundException extends Throwable {
    public AuthorizationNotFoundException() {
        super("Authorization not found");
    }
}
