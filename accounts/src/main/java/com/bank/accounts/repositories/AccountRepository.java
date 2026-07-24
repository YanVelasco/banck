package com.bank.accounts.repositories;

import com.bank.accounts.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    boolean existsByAccountNumber(long number);

    Optional<AccountEntity> findByCustomerId(Long customerId);

    Optional<AccountEntity> findByAccountNumber(Long accountNumber);

    @Transactional
    @Modifying
    void deleteByCustomerId(Long customerId);
}
