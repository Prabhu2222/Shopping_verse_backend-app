package com.example.shoppingverse.transformer;

import com.example.shoppingverse.dto.request.CardRequestDto;
import com.example.shoppingverse.dto.response.CardResponseDto;
import com.example.shoppingverse.model.Card;

public class CardTransformer {
    public static Card CardRequestDtoToCard(CardRequestDto cardRequestDto){
        Card card=Card.builder()
                .cardNo(cardRequestDto.getCardNo())
                .cvv(cardRequestDto.getCvv())
                .validTill(cardRequestDto.getValidTill())
                .cardType(cardRequestDto.getCardType())
                .build();
        return  card;

    }
    public static CardResponseDto CardToCardResponseDto(Card card){
        CardResponseDto responseDto=CardResponseDto.builder()
                .customerName(card.getCustomer().getName())
                .cardNo(card.getCardNo())
                .validTill(card.getValidTill())
                .cardType(card.getCardType())
                .build();
        return responseDto;
    }
}
