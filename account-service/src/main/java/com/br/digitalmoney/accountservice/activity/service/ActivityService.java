package com.br.digitalmoney.accountservice.activity.service;

import com.br.digitalmoney.accountservice.activity.domains.Activity;

import java.util.List;

public interface ActivityService {
    Activity create(Activity activity);
    List<Activity> getAllActivitiesByIdAccounts(Long id);
    Activity getActivityByTransferId(Long id, Long transactionId);
}
