package com.bank.cards.service.impl;

import com.bank.cards.dtos.CardDto;
import com.bank.cards.entities.CardEntity;
import com.bank.cards.enums.CardsConstantsEnum;
import com.bank.cards.exceptions.AlreadyExistsException;
import com.bank.cards.exceptions.NotFoundException;
import com.bank.cards.mapper.CardsMapper;
import com.bank.cards.repository.CardRepository;
import com.bank.cards.service.ICardService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class CardServiceImpl implements ICardService {

    private static final int NEW_CARD_LIMIT = 100_000;

    private final CardRepository cardRepository;

    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public void createCard(String mobileNumber) {
        Optional<CardEntity> optionalCard = cardRepository.findByMobileNumber(mobileNumber);
        if (optionalCard.isPresent()) {
            throw new AlreadyExistsException("Card already registered with given mobileNumber " + mobileNumber);
        }
        cardRepository.save(createNewCard(mobileNumber));
    }

    private CardEntity createNewCard(String mobileNumber) {
        long randomCardNumber = generateRandomCardNumber();
        return CardEntity.builder()
                .mobileNumber(mobileNumber)
                .cardNumber(String.valueOf(randomCardNumber))
                .cardType(CardsConstantsEnum.CREDIT_CARD.getValue())
                .totalLimit(NEW_CARD_LIMIT)
                .amountUsed(0)
                .availableAmount(NEW_CARD_LIMIT)
                .build();
    }

    private long generateRandomCardNumber() {
        long rand = new Random().nextLong() & Long.MAX_VALUE;
        return 100_000_000_000L + rand % 900_000_000_000L;
    }

    @Override
    public CardDto fetchCard(String mobileNumber) {
        CardEntity cards = cardRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new NotFoundException("Card not found with mobile number: " + mobileNumber)
        );
        return CardsMapper.mapToCardsDto(cards);
    }

    @Override
    public boolean updateCard(CardDto cardDto) {
        CardEntity cards = cardRepository.findByCardNumber(cardDto.cardNumber()).orElseThrow(
                () -> new NotFoundException("Card not found with card number: " + cardDto.cardNumber())
        );
        CardsMapper.mapToCards(cardDto, cards);
        cardRepository.save(cards);
        return true;
    }

    @Override
    public boolean deleteCard(String mobileNumber) {
        CardEntity cards = cardRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new NotFoundException("Card not found with mobile number: " + mobileNumber)
        );
        cardRepository.deleteById(cards.getCardId());
        return true;
    }
}
