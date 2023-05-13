package com.br.digitalmoney.accountservice.cards.service.impl;

import com.br.digitalmoney.accountservice.accounts.exception.AccountNotFoundException;
import com.br.digitalmoney.accountservice.accounts.repository.AccountRepository;
import com.br.digitalmoney.accountservice.cards.assembler.CardMapper;
import com.br.digitalmoney.accountservice.cards.domain.Card;
import com.br.digitalmoney.accountservice.cards.entities.CardEntity;
import com.br.digitalmoney.accountservice.cards.exception.CardAlreadyRegisteredException;
import com.br.digitalmoney.accountservice.cards.repository.CardRepository;
import com.br.digitalmoney.accountservice.cards.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.br.digitalmoney.utils.AccountDataTest.getAccountEntityWithCard;
import static com.br.digitalmoney.utils.AccountDataTest.getDefaultAccountEntity;
import static com.br.digitalmoney.utils.CardDataTest.getDefautCard;
import static com.br.digitalmoney.utils.CardDataTest.getDefautCardEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CardMapper cardMapper;
    private CardService cardService;

    @BeforeEach
    void setUp() {
        cardService = new CardServiceImpl(cardRepository, accountRepository, cardMapper);
    }

    @Test
    void createCardReturnSuccess() {
        Long accountId = 1L;
        Card card = getDefautCard();
        CardEntity cardEntity = getDefautCardEntity();

        when(cardRepository.existsCardEntityByNumberAndCvc(anyString(), anyString())).thenReturn(false);
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(getDefaultAccountEntity()));
        when(cardMapper.fromCardToCardEntity(any(Card.class))).thenReturn(cardEntity);
        when(cardRepository.save(any(CardEntity.class))).thenReturn(cardEntity);
        when(cardMapper.fromCardEntityToCard(any(CardEntity.class))).thenReturn(card);

        assertNotNull(cardService.create(accountId, card));
    }

    @Test
    void createCardThrowsCardAlreadyRegisteredException() {
        Long accountId = 1L;
        Card card = getDefautCard();

        when(cardRepository.existsCardEntityByNumberAndCvc(anyString(), anyString())).thenReturn(true);

        assertThrows(CardAlreadyRegisteredException.class, () -> cardService.create(accountId, card));
    }

    @Test
    void createCardThrowsAccountNotFoundException() {
        Long accountId = 1L;
        Card card = getDefautCard();

        when(cardRepository.existsCardEntityByNumberAndCvc(anyString(), anyString())).thenReturn(false);
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> cardService.create(accountId, card));
    }

    @Test
    void deleteCardReturnTrue() {
        Long accountId = 1L;
        Long cardId = 999L;
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(getAccountEntityWithCard()));

        assertTrue(cardService.deleteCard(accountId, cardId));
        verify(cardRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteCardReturnFalse() {
        Long accountId = 1L;
        Long cardId = 999L;
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(getDefaultAccountEntity()));

        assertFalse(cardService.deleteCard(accountId, cardId));
        verify(cardRepository, times(0)).deleteById(anyLong());
    }

    @Test
    void getAllCardsByAccountSuccess() {
        Long accountId = 1L;

        when(accountRepository.existsById(anyLong())).thenReturn(true);
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(getAccountEntityWithCard()));
        when(cardMapper.fromCardEntityToCard(any(CardEntity.class))).thenReturn(getDefautCard());

        assertNotNull(cardService.getAllCardsByAccount(accountId));
    }

    @Test
    void getAllCardsByAccountThrowsAccountNotFoundException() {
        Long accountId = 1L;

        when(accountRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(AccountNotFoundException.class, () -> cardService.getAllCardsByAccount(accountId));
    }

    @Test
    void getByIdCardAndAccountSuccess() {
        Long accountId = 1L;
        Long cardId = 999L;

        when(accountRepository.existsById(anyLong())).thenReturn(true);
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(getAccountEntityWithCard()));
        when(cardMapper.fromCardEntityToCard(any(CardEntity.class))).thenReturn(getDefautCard());

        assertNotNull(cardService.getByIdCardAndAccount(accountId, cardId));
    }

    @Test
    void getByIdCardAndAccountThrowsAccountNotFoundException() {
        Long accountId = 1L;
        Long cardId = 999L;

        when(accountRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(AccountNotFoundException.class, () -> cardService.getByIdCardAndAccount(accountId, cardId));
    }
}