package com.br.digitalmoney.utils;

import com.br.digitalmoney.accountservice.activity.domains.Activity;
import com.br.digitalmoney.accountservice.activity.dto.response.ActivityResponse;
import com.br.digitalmoney.accountservice.activity.entities.ActivityEntity;
import com.br.digitalmoney.accountservice.activity.entities.ActivityType;

import java.time.LocalDateTime;

import static com.br.digitalmoney.utils.AccountDataTest.getDefaultAccountEntity;
import static com.br.digitalmoney.utils.TransactionDataTest.getTransferenceTransactionEntity;

public class ActivityDataTest {

    public static Activity getDefaultActivity(){
        return new Activity(333L, 1L, LocalDateTime.now(), 10.0, ActivityType.IN, "");
    }

    public static ActivityResponse getDefaultActivityResponse(){
        return new ActivityResponse(333L, 1L, LocalDateTime.now(), 10.0, ActivityType.IN, "");
    }

    public static ActivityEntity getDefaultActivityEntity(){
        return ActivityEntity.builder()
                .id(333L)
                .transaction(getTransferenceTransactionEntity())
                .account(getDefaultAccountEntity())
                .value(10.0)
                .activityType(ActivityType.IN)
                .dateTransaction(LocalDateTime.now())
                .description("")
                .build();
    }
}
