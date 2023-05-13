package com.br.digitalmoney.accountservice.transactions.service.impl;

import com.br.digitalmoney.accountservice.accounts.entities.AccountEntity;
import com.br.digitalmoney.accountservice.accounts.exception.AccountNotFoundException;
import com.br.digitalmoney.accountservice.accounts.repository.AccountRepository;
import com.br.digitalmoney.accountservice.activity.service.ActivityService;
import com.br.digitalmoney.accountservice.transactions.assembler.TransactionMapper;
import com.br.digitalmoney.accountservice.transactions.domain.Transaction;
import com.br.digitalmoney.accountservice.transactions.entity.TransactionEntity;
import com.br.digitalmoney.accountservice.transactions.entity.TransactionType;
import com.br.digitalmoney.accountservice.transactions.exception.TransactionNotAuthorizedException;
import com.br.digitalmoney.accountservice.transactions.repository.TransactionCriteriaRepository;
import com.br.digitalmoney.accountservice.transactions.repository.TransactionRepository;
import com.br.digitalmoney.accountservice.transactions.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.br.digitalmoney.utils.AccountDataTest.*;
import static com.br.digitalmoney.utils.TransactionDataTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionMapper transactionMapper;
    @Mock
    private ActivityService activityService;
    private TransactionService transactionService;

    @Mock
    private TransactionCriteriaRepository transactionCriteriaRepository;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionServiceImpl(transactionRepository, accountRepository, transactionMapper, activityService, transactionCriteriaRepository);
    }

    @Test
    void createDepositAndReturnSuccess() {
        Long id = 333L;
        Transaction transaction = getDepositTransaction();
        TransactionEntity transactionEntity = getDepositTransactionEntity();
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(getDefaultAccountEntity()));
        when(transactionMapper.fromTransactionToTransactionEntity(any(Transaction.class))).thenReturn(transactionEntity);
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(transactionEntity);
        when(accountRepository.save(any(AccountEntity.class))).thenReturn(getAccountEntityWithDeposit());
        when(transactionMapper.fromTransactionEntityToTransaction(any(TransactionEntity.class))).thenReturn(transaction);

        Transaction response = transactionService.create(id, transaction);
        assertNotNull(response);
        assertEquals(response.amount(), transaction.amount());
        assertEquals(response.transactionType(), transaction.transactionType());
        assertEquals(response.accountIDTo(), transaction.accountIDTo());
        assertEquals(response.accountIDFrom(), transaction.accountIDFrom());
        assertEquals(response.date(), transaction.date());
    }

    @Test
    void createDepositAndThrowsAccountNotFoundException() {
        Long id = 333L;
        Transaction transaction = getDepositTransaction();

        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> transactionService.create(id, transaction));
    }

    @Test
    void transferenceAndReturnSuccess() {
        Long id = 333L;
        Transaction transaction = getTransferenceTransaction();
        TransactionEntity transactionEntity = getTransferenceTransactionEntity();
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(getDefaultAccountEntity()));
        when(transactionMapper.fromTransactionToTransactionEntity(any(Transaction.class))).thenReturn(transactionEntity);
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(transactionEntity);
        when(accountRepository.saveAll(any())).thenReturn(List.of(new AccountEntity(), new AccountEntity()));
        when(transactionMapper.fromTransactionEntityToTransaction(any(TransactionEntity.class))).thenReturn(transaction);

        Transaction response = transactionService.transference(id, transaction);
        assertNotNull(response);
        assertEquals(response.amount(), transaction.amount());
        assertEquals(response.transactionType(), transaction.transactionType());
        assertEquals(response.accountIDTo(), transaction.accountIDTo());
        assertEquals(response.accountIDFrom(), transaction.accountIDFrom());
        assertEquals(response.date(), transaction.date());
    }

    @Test
    void transferenceThrowAccountNotFoundException() {
        Long id = 333L;
        Transaction transaction = getTransferenceTransaction();

        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> transactionService.transference(id, transaction));
    }

    @Test
    void transferenceThrowTransactionNotAuthorizedException() {
        Long id = 333L;
        Transaction transaction = getTransferenceTransaction();

        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(getAccountEntityNoBalance()));

        assertThrows(TransactionNotAuthorizedException.class, () -> transactionService.transference(id, transaction));
    }

    @Test
    void findLastTransactionsByAccountIDAndReturnSuccess() {
        Long id = 1L;
        Transaction transaction = getTransferenceTransaction();

        when(transactionRepository.findFirst5ByAccountFromIdOrAccountToIdOrderByDateDesc(anyLong(), any())).thenReturn(List.of(getTransferenceTransactionEntity()));
        when(transactionMapper.fromTransactionEntityToTransaction(any(TransactionEntity.class))).thenReturn(transaction);

        List<Transaction> response = transactionService.findLastTransactionsByAccountID(id);
        assertNotNull(response);
        assertEquals(response.size(), 1);
        assertEquals(response.get(0).transactionType(), TransactionType.TRANSFERENCE);
    }
}