package com.bank.cards.repository;

import com.bank.cards.entities.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<CardEntity, Long> {
    Optional<CardEntity> findByMobileNumber(String mobileNumber);

    Optional<CardEntity> findByCardNumber(String cardNumber);
}