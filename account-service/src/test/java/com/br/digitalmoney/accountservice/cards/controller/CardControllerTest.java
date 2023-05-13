package com.br.digitalmoney.accountservice.cards.controller;

import com.br.digitalmoney.accountservice.cards.assembler.CardMapper;
import com.br.digitalmoney.accountservice.cards.domain.Card;
import com.br.digitalmoney.accountservice.cards.dto.request.CardRequest;
import com.br.digitalmoney.accountservice.cards.dto.response.CardResponse;
import com.br.digitalmoney.accountservice.cards.entities.CardType;
import com.br.digitalmoney.accountservice.cards.service.CardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static com.br.digitalmoney.utils.CardDataTest.getDefautCard;
import static com.br.digitalmoney.utils.CardDataTest.getDefautCardResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CardController.class)
public class CardControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    private final String URL = "/accounts";

    @MockBean
    private CardMapper cardMapper;

    @MockBean
    private CardService cardService;

    @Test
    public void whenCreateCardShouldReturnCreated() throws Exception {
        LocalDate data = LocalDate.of(2023, 3, 7);
        final var card = new Card(
                "5547614090655751",
                "Silas",
                CardType.DEBIT,
                data.atStartOfDay(),
                "353"

        );

        final var cardRequest = new CardRequest(
                "5547614090655751",
                "Silas",
                CardType.DEBIT.toString(),
                data.atStartOfDay(),
                "353"
        );

        final var bodyRequest = mapper.writeValueAsString(cardRequest);

        final var cardResponse = new CardResponse("5547614090655751",
                "Silas",
                CardType.DEBIT,
                data.atStartOfDay(),
                "353");

        when(cardService.create(anyLong(), any())).thenReturn(card);
        when(cardMapper.fromCardRequestToCard(any())).thenReturn(card);
        when(cardMapper.fromCardToCardResponse(any())).thenReturn(cardResponse);

        mockMvc.perform(post(URL + "/" + 1 + "/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRequest))
                .andExpect(status().isCreated());
    }

    @Test
    void deleteCardShouldReturnOk() throws Exception {
        long accountId = 1L;
        long cardId = 1L;

        when(cardService.deleteCard(anyLong(), anyLong())).thenReturn(true);

        mockMvc.perform(delete(URL + "/" + accountId + "/cards/" + cardId))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCardShouldReturnNotFound() throws Exception {
        long accountId = 1L;
        long cardId = 1L;

        when(cardService.deleteCard(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(delete(URL + "/" + accountId + "/cards/" + cardId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCardByIdAccountShouldReturnOk() throws Exception {
        long accountId = 1L;

        when(cardService.getAllCardsByAccount(anyLong())).thenReturn(List.of(getDefautCard()));
        when(cardMapper.fromCardToCardResponse(any())).thenReturn(getDefautCardResponse());

        mockMvc.perform(get(URL + "/" + accountId + "/cards"))
                .andExpect(status().isOk());
    }

    @Test
    void getByIdCardAndAccountShouldReturnOk() throws Exception {
        long accountId = 1L;
        long cardId = 999L;

        when(cardService.getAllCardsByAccount(anyLong())).thenReturn(List.of(getDefautCard()));
        when(cardMapper.fromCardToCardResponse(any())).thenReturn(getDefautCardResponse());

        mockMvc.perform(get(URL + "/" + accountId + "/cards/" + cardId))
                .andExpect(status().isOk());
    }
}