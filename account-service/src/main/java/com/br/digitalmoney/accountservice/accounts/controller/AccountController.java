package com.br.digitalmoney.accountservice.accounts.controller;

import com.br.digitalmoney.accountservice.accounts.assembler.AccountMapper;
import com.br.digitalmoney.accountservice.accounts.domain.Account;
import com.br.digitalmoney.accountservice.accounts.dto.request.AccountRequest;
import com.br.digitalmoney.accountservice.accounts.dto.response.AccountResponse;
import com.br.digitalmoney.accountservice.accounts.service.AccountService;
import com.br.digitalmoney.accountservice.core.api.OpenApi;
import com.br.digitalmoney.accountservice.core.web.CheckAuthorization;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
public class AccountController implements OpenApi {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @PostMapping
    @CheckAuthorization
    ResponseEntity<AccountResponse> create(@RequestBody AccountRequest accountRequest) {
        final Account account = accountMapper.fromAccountRequestToAccount(accountRequest);
        final Account result = accountService.create(account);
        final AccountResponse accountResponse = accountMapper.fromAccountToAccountResponse(result);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountResponse);
    }

    @GetMapping("{id}")
    @CheckAuthorization
    ResponseEntity<AccountResponse> getAccount(@PathVariable Long id) {
        final Account result = accountService.findByID(id);
        final AccountResponse accountResponse = accountMapper.fromAccountToAccountResponse(result);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountResponse);
    }

    @PatchMapping("{id}")
    @CheckAuthorization
    ResponseEntity<Void> updateAccount(@PathVariable Long id, @RequestBody AccountRequest accountRequest) {
        final Account accountToBeUpdated = accountMapper.fromAccountRequestToAccount(accountRequest);
        accountService.update(id, accountToBeUpdated);

        return ResponseEntity.noContent().build();
    }
}
