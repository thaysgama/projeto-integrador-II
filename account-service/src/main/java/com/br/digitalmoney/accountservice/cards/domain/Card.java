package com.br.digitalmoney.accountservice.cards.domain;

import com.br.digitalmoney.accountservice.cards.entities.CardType;

import java.time.LocalDateTime;

public record Card(
        String number,
        String name,
        CardType cardType,
        LocalDateTime dateExpire,
        String cvc
) {
}
