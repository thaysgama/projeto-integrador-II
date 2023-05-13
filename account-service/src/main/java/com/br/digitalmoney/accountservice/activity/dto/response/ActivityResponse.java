package com.br.digitalmoney.accountservice.activity.dto.response;

import com.br.digitalmoney.accountservice.activity.entities.ActivityType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record ActivityResponse (
        @JsonProperty(value = "transaction_id")
        Long transactionId,
        @JsonProperty(value = "account_id")
        Long accountId,
        @JsonProperty(value = "date_transaction")
        LocalDateTime dateTransaction,
        Double value,
        @JsonProperty(value = "activity_type")
        ActivityType activityType,
        String description
) { }
