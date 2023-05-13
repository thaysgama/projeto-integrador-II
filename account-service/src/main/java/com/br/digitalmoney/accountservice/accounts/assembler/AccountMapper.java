package com.br.digitalmoney.accountservice.accounts.assembler;

import com.br.digitalmoney.accountservice.accounts.domain.Account;
import com.br.digitalmoney.accountservice.accounts.dto.request.AccountRequest;
import com.br.digitalmoney.accountservice.accounts.dto.response.AccountResponse;
import com.br.digitalmoney.accountservice.accounts.entities.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "initialBalance", target = "availableAmount")
    Account fromAccountRequestToAccount(AccountRequest accountRequest);
    AccountResponse fromAccountToAccountResponse(Account account);
    AccountEntity fromAccountToAccountEntity(Account account);
    Account fromAccountEntityToAccount(AccountEntity accountEntity);
}
