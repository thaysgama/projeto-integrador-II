package com.br.digitalmoney.accountservice.accounts.domain;

public record Account(
        Long id,
        Long userId,
        String accountType,
        double availableAmount
) {
}
