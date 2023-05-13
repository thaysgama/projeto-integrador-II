package com.br.digitalmoney.accountservice.accounts.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccountResponse(
        Long id,
        @JsonProperty(value = "account_type")
        String accountType,
        @JsonProperty(value = "available_amount")
        double availableAmount,
        @JsonProperty(value = "user_id")
        Long userId
) {
}
