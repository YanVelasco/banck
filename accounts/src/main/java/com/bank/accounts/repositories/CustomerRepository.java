package com.bank.accounts.repositories;

import com.bank.accounts.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findByMobileNumberOrEmail(String mobileNumber, String email);
}
