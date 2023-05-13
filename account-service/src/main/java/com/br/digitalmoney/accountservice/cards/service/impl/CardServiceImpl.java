package com.br.digitalmoney.accountservice.cards.service.impl;

import com.br.digitalmoney.accountservice.accounts.entities.AccountEntity;
import com.br.digitalmoney.accountservice.accounts.exception.AccountNotFoundException;
import com.br.digitalmoney.accountservice.accounts.repository.AccountRepository;
import com.br.digitalmoney.accountservice.cards.assembler.CardMapper;
import com.br.digitalmoney.accountservice.cards.domain.Card;
import com.br.digitalmoney.accountservice.cards.entities.CardEntity;
import com.br.digitalmoney.accountservice.cards.exception.CardAlreadyRegisteredException;
import com.br.digitalmoney.accountservice.cards.exception.CardNotFoundException;
import com.br.digitalmoney.accountservice.cards.repository.CardRepository;
import com.br.digitalmoney.accountservice.cards.service.CardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
@Transactional
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private final CardMapper cardMapper;

    @Override
    public Card create(Long id, Card card) {
        if(cardRepository.existsCardEntityByNumberAndCvc(card.number(), card.cvc())) {
            throw new CardAlreadyRegisteredException();
        }

        final var accountEntity = accountRepository
                .findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        final var entity = cardMapper.fromCardToCardEntity(card);
        entity.setAccountEntity(accountEntity);
        final var result = cardRepository.save(entity);
        return cardMapper.fromCardEntityToCard(result);
    }

    @Override
    public Boolean deleteCard(Long idAccount, Long idCard) {
        AccountEntity account = accountRepository.findById(idAccount).get();
        for (CardEntity card : account.getCards()) {
            if (idCard.equals(card.getId())) {
                cardRepository.deleteById(idCard);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Card> getAllCardsByAccount(Long id) {
        if(!accountRepository.existsById(id)) {
            throw new AccountNotFoundException(id);
        }
        AccountEntity account = accountRepository.findById(id).get();
        List<Card> cards = new ArrayList<>();
        for (CardEntity card : account.getCards()) {
            cards.add(cardMapper.fromCardEntityToCard(card));
        }
        return cards;
    }

    @Override
    public Card getByIdCardAndAccount(Long idAccount, Long idCard) {
        if(!accountRepository.existsById(idAccount)) {
            throw new AccountNotFoundException(idAccount);
        }
        AccountEntity account = accountRepository.findById(idAccount).get();
        for (CardEntity card : account.getCards()) {
            if (idCard.equals(card.getId())) {
                return cardMapper.fromCardEntityToCard(card);
            }
        }
        throw new CardNotFoundException();
    }
}
