package com.br.digitalmoney.accountservice.accounts.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Long id) {
        super(String.format("account with id %s not found", id));
    }
}
