package com.br.digitalmoney.accountservice.cards.dto.response;

import com.br.digitalmoney.accountservice.cards.entities.CardType;

import java.time.LocalDateTime;

public record CardResponse (
    String number,
    String name,
    CardType cardType,
    LocalDateTime dateExpire,
    String cvc
) {}
