package com.br.digitalmoney.accountservice.transactions.exception;

public class TransactionNotAuthorizedException extends RuntimeException {

    public TransactionNotAuthorizedException(double value) {
        super(String.format("insufficient funds to withdraw $ %s", value));
    }
}
