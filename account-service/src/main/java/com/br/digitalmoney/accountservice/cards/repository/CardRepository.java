package com.br.digitalmoney.accountservice.cards.repository;

import com.br.digitalmoney.accountservice.cards.entities.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, Long> {

    Boolean existsCardEntityByNumberAndCvc(String number, String cvc);
}
