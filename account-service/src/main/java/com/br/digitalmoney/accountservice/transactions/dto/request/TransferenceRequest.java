package com.br.digitalmoney.accountservice.transactions.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record TransferenceRequest(
        @NotNull(message = "the amount must not be null")
        @DecimalMin(value = "0.01", message = "the amount must not be zero or negative")
        double value,
        @NotNull(message = "the account id must not be null")
        @JsonProperty("account_id_to")
        Long accountIDTo,
        String description
) {
}
