package com.bank.cards.mapper;

import com.bank.cards.dtos.CardDto;
import com.bank.cards.entities.CardEntity;

public class CardsMapper {

    public static CardDto mapToCardsDto(CardEntity cardEntity) {
        return CardDto.builder()
                .mobileNumber(cardEntity.getMobileNumber())
                .cardNumber(cardEntity.getCardNumber())
                .cardType(cardEntity.getCardType())
                .totalLimit(cardEntity.getTotalLimit())
                .amountUsed(cardEntity.getAmountUsed())
                .availableAmount(cardEntity.getAvailableAmount())
                .build();
    }

    public static void mapToCards(CardDto cardDto, CardEntity cardEntity) {
        cardEntity.setMobileNumber(cardDto.mobileNumber());
        cardEntity.setCardNumber(cardDto.cardNumber());
        cardEntity.setCardType(cardDto.cardType());
        cardEntity.setTotalLimit(cardDto.totalLimit());
        cardEntity.setAmountUsed(cardDto.amountUsed());
        cardEntity.setAvailableAmount(cardDto.availableAmount());
    }
}
