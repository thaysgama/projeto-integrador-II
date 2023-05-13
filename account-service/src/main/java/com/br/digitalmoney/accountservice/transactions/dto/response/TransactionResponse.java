package com.br.digitalmoney.accountservice.transactions.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record TransactionResponse(
        double amount,
        @JsonProperty("transaction_type")
        String transactionType,
        @JsonProperty("account_id_from")
        Long accountIDFrom,
        @JsonProperty("account_id_to")
        Long accountIDTo,
        LocalDateTime date,
        String description
) {
}
