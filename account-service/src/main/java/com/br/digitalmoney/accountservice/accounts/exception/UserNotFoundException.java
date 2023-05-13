package com.br.digitalmoney.accountservice.accounts.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super(String.format("user with id %s not found", id));
    }
}
