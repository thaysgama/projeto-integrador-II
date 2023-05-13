package com.br.digitalmoney.accountservice.transactions.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(Long id) {
        super(String.format("transaction with id %s not found", id));
    }
}
