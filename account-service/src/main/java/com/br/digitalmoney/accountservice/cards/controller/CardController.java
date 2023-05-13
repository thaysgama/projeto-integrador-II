package com.br.digitalmoney.accountservice.cards.controller;

import com.br.digitalmoney.accountservice.cards.assembler.CardMapper;
import com.br.digitalmoney.accountservice.cards.dto.request.CardRequest;
import com.br.digitalmoney.accountservice.cards.dto.response.CardResponse;
import com.br.digitalmoney.accountservice.cards.service.CardService;
import com.br.digitalmoney.accountservice.core.api.OpenApi;
import com.br.digitalmoney.accountservice.core.web.CheckAuthorization;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class CardController implements OpenApi {
    private final CardService cardService;
    private final CardMapper cardMapper;

    @PostMapping("/{id}/cards")
    @CheckAuthorization
    ResponseEntity<CardResponse> create(@PathVariable Long id, @RequestBody @Valid CardRequest cardRequest) {
        final var card = cardMapper.fromCardRequestToCard(cardRequest);
        final var result = cardService.create(id, card);
        CardResponse cardResponse = cardMapper.fromCardToCardResponse(result);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cardResponse);
    }

    @DeleteMapping("/{idAccount}/cards/{idCard}")
    @CheckAuthorization
    ResponseEntity<String> deleteCard(@PathVariable Long idAccount, @PathVariable Long idCard) {
        Boolean deleteCardresponse = cardService.deleteCard(idAccount, idCard);

        if (deleteCardresponse) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Deleted.");
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Not found.");
    }

    @GetMapping("/{id}/cards")
    @CheckAuthorization
    ResponseEntity<List<CardResponse>> getCardByIdAccount(@PathVariable Long id) {
        final var response = cardService.getAllCardsByAccount(id);
        List<CardResponse> cards = new ArrayList<>();
        for (var card : response) {
            cards.add(cardMapper.fromCardToCardResponse(card));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cards);
    }

    @GetMapping("/{idAccount}/cards/{idCard}")
    @CheckAuthorization
    ResponseEntity<CardResponse> getByIdCardAndAccount(@PathVariable Long idAccount, @PathVariable Long idCard) {
        final var response = cardService.getByIdCardAndAccount(idAccount, idCard);
        final var result = cardMapper.fromCardToCardResponse(response);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }
}
