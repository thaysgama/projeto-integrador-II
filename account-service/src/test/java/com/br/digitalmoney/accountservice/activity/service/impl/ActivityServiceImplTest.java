package com.br.digitalmoney.accountservice.activity.service.impl;

import com.br.digitalmoney.accountservice.accounts.exception.AccountNotFoundException;
import com.br.digitalmoney.accountservice.accounts.repository.AccountRepository;
import com.br.digitalmoney.accountservice.activity.assembler.ActivityMapper;
import com.br.digitalmoney.accountservice.activity.domains.Activity;
import com.br.digitalmoney.accountservice.activity.repository.ActivityRepository;
import com.br.digitalmoney.accountservice.activity.service.ActivityService;
import com.br.digitalmoney.accountservice.transactions.exception.TransactionNotFoundException;
import com.br.digitalmoney.accountservice.transactions.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static com.br.digitalmoney.utils.AccountDataTest.getDefaultAccountEntity;
import static com.br.digitalmoney.utils.ActivityDataTest.getDefaultActivity;
import static com.br.digitalmoney.utils.ActivityDataTest.getDefaultActivityEntity;
import static com.br.digitalmoney.utils.TransactionDataTest.getTransferenceTransactionEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActivityServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private ActivityRepository activityRepository;
    @Mock
    private ActivityMapper activityMapper;
    private ActivityService activityService;

    @BeforeEach
    void setUp() {
        activityService = new ActivityServiceImpl(transactionRepository, accountRepository, activityRepository, activityMapper);
    }

    @Test
    void createShouldReturnSuccess() {
        Activity activity = getDefaultActivity();

        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(getDefaultAccountEntity()));
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(getTransferenceTransactionEntity()));
        when(activityMapper.fromActivityToActivityEntity(any())).thenReturn(getDefaultActivityEntity());
        when(activityMapper.fromActivityEntityToActivity(any())).thenReturn(getDefaultActivity());

        assertNotNull(activityService.create(activity));
    }

    @Test
    void getAllActivitiesByIdAccountsShouldReturnSuccess() {
        Activity activity = getDefaultActivity();

        when(accountRepository.existsById(anyLong())).thenReturn(true);
        when(activityRepository.findAll(Sort.by(Sort.Direction.DESC, "dateTransaction"))).thenReturn(List.of(getDefaultActivityEntity()));
        when(activityMapper.fromActivityEntityToActivity(any())).thenReturn(getDefaultActivity());

        List<Activity> response = activityService.getAllActivitiesByIdAccounts(activity.accountId());
        assertNotNull(response);
        assertEquals(response.size(), 1);
    }

    @Test
    void getAllActivitiesByIdAccountsShouldThrowAccountNotFoundException() {
        Activity activity = getDefaultActivity();

        when(accountRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(AccountNotFoundException.class, () -> activityService.getAllActivitiesByIdAccounts(activity.accountId()));
    }

    @Test
    void getActivityByTransferIdShouldReturnSuccess() {
        Activity activity = getDefaultActivity();

        when(accountRepository.existsById(anyLong())).thenReturn(true);
        when(transactionRepository.existsById(anyLong())).thenReturn(true);
        when(activityRepository.findByAccountIdAndTransactionId(any(), any())).thenReturn(getDefaultActivityEntity());
        when(activityMapper.fromActivityEntityToActivity(any())).thenReturn(getDefaultActivity());

        Activity response = activityService.getActivityByTransferId(activity.accountId(), activity.transactionId());
        assertNotNull(response);
        assertEquals(response.activityType(), activity.activityType());
    }

    @Test
    void getActivityByTransferIdShouldThrowAccountNotFoundException() {
        Activity activity = getDefaultActivity();

        when(accountRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(AccountNotFoundException.class, () -> activityService.getActivityByTransferId(activity.accountId(), activity.transactionId()));
    }

    @Test
    void getActivityByTransferIdShouldThrowTransactionNotFrounException() {
        Activity activity = getDefaultActivity();

        when(accountRepository.existsById(anyLong())).thenReturn(true);
        when(transactionRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(TransactionNotFoundException.class, () -> activityService.getActivityByTransferId(activity.accountId(), activity.transactionId()));
    }
}