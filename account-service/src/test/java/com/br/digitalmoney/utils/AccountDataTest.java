package com.br.digitalmoney.utils;

import com.br.digitalmoney.accountservice.accounts.domain.Account;
import com.br.digitalmoney.accountservice.accounts.dto.request.AccountRequest;
import com.br.digitalmoney.accountservice.accounts.dto.response.AccountResponse;
import com.br.digitalmoney.accountservice.accounts.entities.AccountEntity;
import com.br.digitalmoney.accountservice.accounts.entities.AccountType;

import java.util.*;

import static com.br.digitalmoney.utils.CardDataTest.getDefautCardEntity;

public class AccountDataTest {

    public static Account getDefaultAccount(){
        return new Account(1L, 123L, "DEPOSIT_ACCOUNT", 20.0);
    }

    public static AccountResponse getDefaultAccountResponse(){
        return new AccountResponse(1L, "DEPOSIT_ACCOUNT",  20.0,123L);
    }

    public static AccountRequest getDefaultAccountRequest(){
        return new AccountRequest(123L, "DEPOSIT_ACCOUNT",  20.0);
    }

    public static AccountEntity getDefaultAccountEntity(){
        return AccountEntity.builder()
                .id(1L)
                .userId(123L)
                .accountType(AccountType.DEPOSIT_ACCOUNT)
                .availableAmount(20.0)
                .cards(new ArrayList<>())
                .transactionsTo(new ArrayList<>())
                .transactionsFrom(new ArrayList<>())
                .build();
    }

    public static AccountEntity getAccountEntityWithDeposit(){
        return AccountEntity.builder()
                .id(1L)
                .userId(123L)
                .accountType(AccountType.DEPOSIT_ACCOUNT)
                .availableAmount(220.0)
                .build();
    }

    public static AccountEntity getAccountEntityWithCard(){
        return AccountEntity.builder()
                .id(1L)
                .userId(123L)
                .accountType(AccountType.DEPOSIT_ACCOUNT)
                .availableAmount(20.0)
                .cards(List.of(getDefautCardEntity()))
                .transactionsTo(new ArrayList<>())
                .transactionsFrom(new ArrayList<>())
                .build();
    }

    public static AccountEntity getAccountEntityNoBalance(){
        return AccountEntity.builder()
                .id(1L)
                .userId(123L)
                .accountType(AccountType.DEPOSIT_ACCOUNT)
                .availableAmount(0.0)
                .cards(new ArrayList<>())
                .transactionsTo(new ArrayList<>())
                .transactionsFrom(new ArrayList<>())
                .build();
    }
}
