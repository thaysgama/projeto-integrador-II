package com.br.digitalmoney.accountservice.cards.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;


public record CardRequest (
        @Size(min = 16, max = 16, message = "there must be 16 numbers")
        @NotBlank(message = "the number must not be null or empty")
        String number,
        @NotBlank(message = "the name must not be null or empty")
        String name,
        @NotBlank(message = "the card type must not be null or empty")
        @JsonProperty("card_type")
        String cardType,
        @JsonProperty("date_expire")
        LocalDateTime dateExpire,
        @Size(min = 3, max = 3, message = "the cvc must be 3 numbers")
        @NotBlank(message = "the cvc must not be null or empty")
        String cvc
) {}
