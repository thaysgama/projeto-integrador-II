package com.br.digitalmoney.accountservice.transactions.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TransactionRequest(
        @NotNull(message = "the amount must not be null")
        @DecimalMin(value = "0.01", message = "the amount must not be zero or negative")
        double value,
        @JsonProperty("transaction_type")
        String transactionType,
        @JsonProperty("account_id_to")
        Long accountIDTo,
        String description
) {
        TransactionRequest withAccountIDTo(Long id) {
                return new TransactionRequest(
                        value(),
                        transactionType(),
                        id,
                        description()
                );
        }
}
