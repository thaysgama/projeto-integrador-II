package com.br.digitalmoney.accountservice.activity.domains;

import com.br.digitalmoney.accountservice.activity.entities.ActivityType;

import java.time.LocalDateTime;

public record Activity(
        Long transactionId,
        Long accountId,
        LocalDateTime dateTransaction,
        Double value,
        ActivityType activityType,
        String description
) { }
