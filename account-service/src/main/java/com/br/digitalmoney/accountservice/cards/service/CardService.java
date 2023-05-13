package com.br.digitalmoney.accountservice.cards.service;

import com.br.digitalmoney.accountservice.cards.domain.Card;
import com.br.digitalmoney.accountservice.cards.dto.response.CardResponse;

import java.util.List;

public interface CardService {
    Card create(Long id, Card card);
    Boolean deleteCard(Long idAccount, Long idCard);
    List<Card> getAllCardsByAccount(Long id);
    Card getByIdCardAndAccount(Long idAccount, Long idCard);
}
