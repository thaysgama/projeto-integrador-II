package com.br.digitalmoney.accountservice.accounts.service.impl;

import com.br.digitalmoney.accountservice.accounts.assembler.AccountMapper;
import com.br.digitalmoney.accountservice.accounts.domain.Account;
import com.br.digitalmoney.accountservice.accounts.entities.AccountEntity;
import com.br.digitalmoney.accountservice.accounts.exception.AccountNotFoundException;
import com.br.digitalmoney.accountservice.accounts.exception.UserNotFoundException;
import com.br.digitalmoney.accountservice.accounts.repository.AccountRepository;
import com.br.digitalmoney.accountservice.accounts.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    public Account create(Account account) {
        final AccountEntity entity = accountMapper.fromAccountToAccountEntity(account);

        final AccountEntity result = accountRepository.save(entity);
        return accountMapper.fromAccountEntityToAccount(result);
    }

    @Override
    public Account findByID(Long id) {
        final AccountEntity result = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        return accountMapper.fromAccountEntityToAccount(result);
    }

    @Override
    public Account update(Long id, Account account) {
        accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        final AccountEntity accountEntityToBeUpdated = accountMapper.fromAccountToAccountEntity(account);
        accountEntityToBeUpdated.setId(id);
        final AccountEntity result = accountRepository.save(accountEntityToBeUpdated);

        return accountMapper.fromAccountEntityToAccount(result);
    }
}
