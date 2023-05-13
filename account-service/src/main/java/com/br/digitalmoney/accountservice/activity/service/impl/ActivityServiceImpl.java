package com.br.digitalmoney.accountservice.activity.service.impl;

import com.br.digitalmoney.accountservice.accounts.exception.AccountNotFoundException;
import com.br.digitalmoney.accountservice.accounts.repository.AccountRepository;
import com.br.digitalmoney.accountservice.activity.assembler.ActivityMapper;
import com.br.digitalmoney.accountservice.activity.domains.Activity;
import com.br.digitalmoney.accountservice.activity.entities.ActivityEntity;
import com.br.digitalmoney.accountservice.activity.repository.ActivityRepository;
import com.br.digitalmoney.accountservice.activity.service.ActivityService;
import com.br.digitalmoney.accountservice.transactions.exception.TransactionNotFoundException;
import com.br.digitalmoney.accountservice.transactions.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class ActivityServiceImpl implements ActivityService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;

    @Override
    public Activity create(Activity activity) {
        final var accountEntity = accountRepository
                .findById(activity.accountId());
        final var transactionEntity = transactionRepository
                .findById(activity.transactionId());
        final var entity = activityMapper.fromActivityToActivityEntity(activity);
        entity.setAccount(accountEntity.get());
        entity.setTransaction(transactionEntity.get());
        final var result = activityRepository.save(entity);
        return activityMapper.fromActivityEntityToActivity(result);
    }

    @Override
    public List<Activity> getAllActivitiesByIdAccounts(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new AccountNotFoundException(id);
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "dateTransaction");
        final var result = activityRepository.findAll(sort);
        List<Activity> entities = new ArrayList<>();
        for (ActivityEntity activity : result) {
            if (activity.getAccount().getId().equals(id)) {
                entities.add(activityMapper.fromActivityEntityToActivity(activity));
            }
        }
        return entities;
    }

    @Override
    public Activity getActivityByTransferId(Long id, Long transactionId) {
        if (!accountRepository.existsById(id)) {
            throw new AccountNotFoundException(id);
        }

        if (!transactionRepository.existsById(transactionId)) {
            throw new TransactionNotFoundException(transactionId);
        }

        final var result = activityRepository.findByAccountIdAndTransactionId(id, transactionId);
        return activityMapper.fromActivityEntityToActivity(result);
    }


}
