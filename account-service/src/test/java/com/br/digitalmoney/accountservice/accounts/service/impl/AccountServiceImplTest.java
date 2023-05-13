package com.br.digitalmoney.accountservice.accounts.service.impl;

import com.br.digitalmoney.accountservice.accounts.assembler.AccountMapper;
import com.br.digitalmoney.accountservice.accounts.domain.Account;
import com.br.digitalmoney.accountservice.accounts.entities.AccountEntity;
import com.br.digitalmoney.accountservice.accounts.exception.AccountNotFoundException;
import com.br.digitalmoney.accountservice.accounts.repository.AccountRepository;
import com.br.digitalmoney.accountservice.accounts.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.br.digitalmoney.utils.AccountDataTest.getDefaultAccount;
import static com.br.digitalmoney.utils.AccountDataTest.getDefaultAccountEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountMapper accountMapper;

    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountServiceImpl(accountRepository, accountMapper);
    }

    @Test
    void createAccountReturnSuccess() {
        Account account = getDefaultAccount();
        AccountEntity accountEntity = getDefaultAccountEntity();
        when(accountMapper.fromAccountToAccountEntity(any(Account.class))).thenReturn(accountEntity);
        when(accountRepository.save(any(AccountEntity.class))).thenReturn(accountEntity);
        when(accountMapper.fromAccountEntityToAccount(any(AccountEntity.class))).thenReturn(account);

        Account response = accountService.create(account);
        assertNotNull(response);
        assertEquals(response.id(), account.id());
        assertEquals(response.accountType(), account.accountType());
        assertEquals(response.userId(), account.userId());
        assertEquals(response.availableAmount(), account.availableAmount());
    }

    @Test
    void findByIDReturnSuccess() {
        Account account = getDefaultAccount();
        AccountEntity accountEntity = getDefaultAccountEntity();
        when(accountMapper.fromAccountEntityToAccount(any(AccountEntity.class))).thenReturn(account);
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(accountEntity));

        Account response = accountService.findByID(account.id());
        assertNotNull(response);
        assertEquals(response.id(), account.id());
        assertEquals(response.accountType(), account.accountType());
        assertEquals(response.userId(), account.userId());
        assertEquals(response.availableAmount(), account.availableAmount());
    }

    @Test
    void findByIDThrowAccountNotFoundException() {
        when(accountRepository.findById(anyLong())).thenThrow(AccountNotFoundException.class);

        assertThrows(AccountNotFoundException.class, () -> accountService.findByID(1L));
    }

    @Test
    void updateThrowAccountNotFoundException() {
        Account account = getDefaultAccount();
        when(accountRepository.findById(anyLong())).thenThrow(AccountNotFoundException.class);

        assertThrows(AccountNotFoundException.class, () -> accountService.update(account.id(), account));
    }

    @Test
    void updateReturnSuccess() {
        Account account = getDefaultAccount();
        AccountEntity accountEntity = getDefaultAccountEntity();

        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(accountEntity));
        when(accountMapper.fromAccountToAccountEntity(any(Account.class))).thenReturn(accountEntity);
        when(accountRepository.save(any(AccountEntity.class))).thenReturn(accountEntity);
        when(accountMapper.fromAccountEntityToAccount(any(AccountEntity.class))).thenReturn(account);

        Account response = accountService.update(account.id(), account);
        assertNotNull(response);
        assertEquals(response.id(), account.id());
        assertEquals(response.accountType(), account.accountType());
        assertEquals(response.userId(), account.userId());
        assertEquals(response.availableAmount(), account.availableAmount());
    }
}