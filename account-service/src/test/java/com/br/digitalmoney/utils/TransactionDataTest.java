package com.br.digitalmoney.utils;

import com.br.digitalmoney.accountservice.transactions.domain.Transaction;
import com.br.digitalmoney.accountservice.transactions.dto.request.TransactionRequest;
import com.br.digitalmoney.accountservice.transactions.dto.request.TransferenceRequest;
import com.br.digitalmoney.accountservice.transactions.dto.response.TransactionResponse;
import com.br.digitalmoney.accountservice.transactions.entity.TransactionEntity;
import com.br.digitalmoney.accountservice.transactions.entity.TransactionType;

import java.time.LocalDateTime;

public class TransactionDataTest {

    public static TransactionRequest getDepositTransactionRequest(){
        return new TransactionRequest(200,null, 1L, "");
    }

    public static Transaction getDepositTransaction(){
        return new Transaction(200,null, 1L, 1L, LocalDateTime.now(), "");
    }

    public static TransactionResponse getDepositTransactionResponse(){
        return new TransactionResponse(200.0,TransactionType.DEPOSIT.name(), 1L, 1L, LocalDateTime.now(), "");
    }

    public static TransactionEntity getDepositTransactionEntity(){
        return TransactionEntity.builder()
                .id(333L)
                .amount(200)
                .transactionType(TransactionType.DEPOSIT)
                .date(LocalDateTime.now())
                .description("")
                .build();
    }

    public static TransferenceRequest getTransferenceRequest(){
        return new TransferenceRequest(10, 2L, "");
    }

    public static Transaction getTransferenceTransaction(){
        return new Transaction(10,TransactionType.TRANSFERENCE, 1L, 2L, LocalDateTime.now(), "");
    }

    public static TransactionResponse getTransferenceTransactionResponse(){
        return new TransactionResponse(10.0,TransactionType.TRANSFERENCE.name(), 1L, 2L, LocalDateTime.now(), "");
    }

    public static TransactionEntity getTransferenceTransactionEntity(){
        return TransactionEntity.builder()
                .id(333L)
                .amount(10)
                .transactionType(TransactionType.TRANSFERENCE)
                .date(LocalDateTime.now())
                .description("")
                .build();
    }
}
