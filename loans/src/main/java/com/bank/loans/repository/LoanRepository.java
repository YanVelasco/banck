package com.bank.loans.repository;

import com.bank.loans.entities.LoanEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanRepository extends JpaRepository<LoanEntity, Long> {

    Optional<LoanEntity> findByMobileNumber(String mobileNumber);

    boolean existsByLoanNumber(long number);

    Optional<LoanEntity> findByLoanNumber(Long loanNumber);

}
