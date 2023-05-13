package com.br.digitalmoney.accountservice.accounts.service;

import com.br.digitalmoney.accountservice.accounts.domain.Account;

public interface AccountService {

    Account create(Account account);
    Account findByID(Long id);
    Account update(Long id, Account account);
}
