package com.br.digitalmoney.accountservice.transactions.service.impl;

import com.br.digitalmoney.accountservice.accounts.entities.AccountEntity;
import com.br.digitalmoney.accountservice.accounts.exception.AccountNotFoundException;
import com.br.digitalmoney.accountservice.accounts.repository.AccountRepository;
import com.br.digitalmoney.accountservice.activity.domains.Activity;
import com.br.digitalmoney.accountservice.activity.entities.ActivityType;
import com.br.digitalmoney.accountservice.activity.repository.ActivityRepository;
import com.br.digitalmoney.accountservice.activity.service.ActivityService;
import com.br.digitalmoney.accountservice.transactions.assembler.TransactionMapper;
import com.br.digitalmoney.accountservice.transactions.domain.Transaction;
import com.br.digitalmoney.accountservice.transactions.dto.TransactionPage;
import com.br.digitalmoney.accountservice.transactions.dto.TransactionSearchCriteria;
import com.br.digitalmoney.accountservice.transactions.entity.TransactionEntity;
import com.br.digitalmoney.accountservice.transactions.entity.TransactionType;
import com.br.digitalmoney.accountservice.transactions.exception.TransactionNotAuthorizedException;
import com.br.digitalmoney.accountservice.transactions.repository.TransactionCriteriaRepository;
import com.br.digitalmoney.accountservice.transactions.repository.TransactionRepository;
import com.br.digitalmoney.accountservice.transactions.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;
    private final ActivityService activityService;

    private final TransactionCriteriaRepository transactionCriteriaRepository;


    @Override
    public Transaction create(Long id, Transaction transaction) {
        final AccountEntity accountEntityFrom = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        transaction.withOriginalAccount(id);
        if(isNull(transaction.transactionType()) || transaction.transactionType() == TransactionType.DEPOSIT) {
            transaction
                    .withTransactionType(TransactionType.DEPOSIT)
                    .withDestinationAccount(id);
            final TransactionEntity transactionEntity = transactionMapper.fromTransactionToTransactionEntity(transaction);
            transactionEntity.setDate(LocalDateTime.now());
            transactionEntity.setAccountFrom(accountEntityFrom);
            transactionEntity.setAccountTo(accountEntityFrom);
            final TransactionEntity savedTransaction = transactionRepository.save(transactionEntity);
            accountEntityFrom.insertTransaction(savedTransaction.getAmount());
            accountRepository.save(accountEntityFrom);

            final Activity activity =
                    new Activity(transactionEntity.getId(), accountEntityFrom.getId(), LocalDateTime.now(), transaction.amount(), ActivityType.IN, "Deposit");
            activityService.create(activity);

            return transactionMapper.fromTransactionEntityToTransaction(savedTransaction);
        }

        final AccountEntity accountEntityTo = accountRepository
                .findById(transaction.accountIDTo())
                .orElseThrow(() -> new AccountNotFoundException(transaction.accountIDTo()));

        final TransactionEntity transactionEntity = transactionMapper.fromTransactionToTransactionEntity(transaction);
        transactionEntity.setDate(LocalDateTime.now());
        transactionEntity.setAccountFrom(accountEntityFrom);
        transactionEntity.setAccountTo(accountEntityTo);
        final TransactionEntity savedTransaction = transactionRepository.save(transactionEntity);

        accountEntityFrom.withdrawTransaction(savedTransaction.getAmount());
        accountEntityTo.insertTransaction(savedTransaction.getAmount());

        accountRepository.saveAll(List.of(accountEntityTo, accountEntityFrom));

        final Activity activityTo =
                new Activity(transactionEntity.getId(), accountEntityTo.getId(), LocalDateTime.now(), transaction.amount(), ActivityType.IN, "Transaction");
        activityService.create(activityTo);
        final Activity activityFrom =
                new Activity(transactionEntity.getId(), accountEntityFrom.getId(), LocalDateTime.now(), transaction.amount(), ActivityType.OUT, "Transaction");
        activityService.create(activityFrom);

        return transactionMapper.fromTransactionEntityToTransaction(savedTransaction);
    }

    @Override
    public Transaction transference(Long id, Transaction transaction) {
        final AccountEntity accountFrom = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        final AccountEntity accountTo = accountRepository.findById(transaction.accountIDTo()).orElseThrow(() -> new AccountNotFoundException(transaction.accountIDTo()));

        if(accountFrom.hasNoBalance(transaction.amount())) {
            throw new TransactionNotAuthorizedException(transaction.amount());
        }

        Transaction newTransaction = transaction.withAccountIdAndTransactionType(id, TransactionType.TRANSFERENCE);

        final TransactionEntity transactionEntity = transactionMapper.fromTransactionToTransactionEntity(newTransaction);
        transactionEntity.setDate(LocalDateTime.now());
        transactionEntity.setAccountFrom(accountFrom);
        transactionEntity.setAccountTo(accountTo);
        final TransactionEntity savedTransaction = transactionRepository.save(transactionEntity);

        accountFrom.withdrawTransaction(transaction.amount());

        accountTo.insertTransaction(transaction.amount());

        accountRepository.saveAll(List.of(accountFrom, accountTo));

        final Activity activityFrom =
                new Activity(transactionEntity.getId(), accountFrom.getId(), LocalDateTime.now(), transaction.amount(), ActivityType.OUT, "Transference");
        activityService.create(activityFrom);

        final Activity activityTo =
                new Activity(transactionEntity.getId(), accountTo.getId(), LocalDateTime.now(), transaction.amount(), ActivityType.IN, "Transference");
        activityService.create(activityTo);

        return transactionMapper.fromTransactionEntityToTransaction(savedTransaction);
    }

    @Override
    public List<Transaction> findLastTransferencesByAccountID(Long accountID) {
        return transactionRepository.findFirst10ByAccountFromIdAndTransactionTypeOrderByDateDesc(accountID, TransactionType.TRANSFERENCE)
                .stream().map((t) -> transactionMapper.fromTransactionEntityToTransaction(t))
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findLastTransactionsByAccountID(Long accountID) {
        return transactionRepository.findFirst5ByAccountFromIdOrAccountToIdOrderByDateDesc(accountID, accountID)
                .stream().map((t) -> transactionMapper.fromTransactionEntityToTransaction(t))
                .collect(Collectors.toList());
    }

    @Override
    public Page<TransactionEntity> findByFilters(TransactionPage transactionPage,
                                                 TransactionSearchCriteria transactionSearchCriteria) {
        return transactionCriteriaRepository.findAllWithFilters(transactionPage, transactionSearchCriteria);
    }

}
