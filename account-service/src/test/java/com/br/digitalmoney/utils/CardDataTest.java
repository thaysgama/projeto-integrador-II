package com.br.digitalmoney.utils;

import com.br.digitalmoney.accountservice.cards.domain.Card;
import com.br.digitalmoney.accountservice.cards.dto.response.CardResponse;
import com.br.digitalmoney.accountservice.cards.entities.CardEntity;
import com.br.digitalmoney.accountservice.cards.entities.CardType;

import java.time.LocalDateTime;

public class CardDataTest {

    public static Card getDefautCard(){
        return new Card("1258447692", "Toim Silva", CardType.DEBIT, LocalDateTime.now().plusYears(3), "557");
    }

    public static CardResponse getDefautCardResponse(){
        return new CardResponse("1258447692", "Toim Silva", CardType.DEBIT, LocalDateTime.now().plusYears(3), "557");
    }

    public static CardEntity getDefautCardEntity(){
        return CardEntity.builder()
                .id(999L)
                .number("1258447692")
                .name("Toim Silva")
                .cardType(CardType.DEBIT)
                .dateExpire(LocalDateTime.now().plusYears(3))
                .cvc("557")
                .build();
    }
}
