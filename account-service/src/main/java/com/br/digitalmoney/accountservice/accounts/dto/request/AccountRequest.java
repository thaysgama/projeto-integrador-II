package com.br.digitalmoney.accountservice.accounts.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AccountRequest(
        @NotBlank(message = "user id must not be null or empty")
        @JsonProperty("user_id")
        Long userId,
        @NotBlank(message = "account type must not be null or empty")
        @JsonProperty("account_type")
        String accountType,
        @NotNull(message = "availableAmount must no be null")
        @JsonProperty("initial_balance")
        double initialBalance
) {
}
