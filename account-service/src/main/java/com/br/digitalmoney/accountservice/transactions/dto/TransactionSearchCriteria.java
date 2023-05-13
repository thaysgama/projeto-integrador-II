package com.br.digitalmoney.accountservice.transactions.dto;

import com.br.digitalmoney.accountservice.transactions.entity.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionSearchCriteria {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double minAmount;
    private Double maxAmount;
    private TransactionType transactionType;
}
