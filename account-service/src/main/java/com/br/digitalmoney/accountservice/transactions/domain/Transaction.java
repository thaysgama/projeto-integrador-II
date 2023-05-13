package com.br.digitalmoney.accountservice.transactions.domain;

import com.br.digitalmoney.accountservice.transactions.entity.TransactionType;

import java.time.LocalDateTime;

public record Transaction(
        double amount,
        TransactionType transactionType,
        Long accountIDFrom,
        Long accountIDTo,
        LocalDateTime date,
        String description
) {

    public Transaction withTransactionType(TransactionType t) {
        return new Transaction(
                amount(),
                t,
                accountIDFrom(),
                accountIDTo(),
                date(),
                description()
        );
    }

    public Transaction withDestinationAccount(Long accountIDTo) {
        return new Transaction(
                amount(),
                transactionType(),
                accountIDFrom(),
                accountIDTo,
                date(),
                description()
        );
    }

    public Transaction withOriginalAccount(Long accountIDFrom) {
        return new Transaction(
                amount(),
                transactionType(),
                accountIDFrom,
                accountIDTo(),
                date(),
                description()
        );
    }

    public Transaction withAccountIdAndTransactionType(Long accountIDFrom, TransactionType t) {
        return new Transaction(
                amount(),
                t,
                accountIDFrom,
                accountIDTo(),
                date(),
                description()
        );
    }
}
