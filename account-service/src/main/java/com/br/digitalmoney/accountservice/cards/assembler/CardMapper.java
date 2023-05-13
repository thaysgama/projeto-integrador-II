package com.br.digitalmoney.accountservice.cards.assembler;

import com.br.digitalmoney.accountservice.cards.domain.Card;
import com.br.digitalmoney.accountservice.cards.dto.request.CardRequest;
import com.br.digitalmoney.accountservice.cards.dto.response.CardResponse;
import com.br.digitalmoney.accountservice.cards.entities.CardEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardMapper {
    
    Card fromCardRequestToCard(CardRequest cardRequest);
    CardResponse fromCardToCardResponse(Card card);
    CardEntity fromCardToCardEntity(Card card);
    Card fromCardEntityToCard(CardEntity cardEntity);
    CardResponse fromCardEntityToCardResponse(CardEntity cardEntity);
}
