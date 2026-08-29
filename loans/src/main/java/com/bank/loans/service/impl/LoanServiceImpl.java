package com.bank.loans.service.impl;

import com.bank.loans.dtos.LoanDto;
import com.bank.loans.entities.LoanEntity;
import com.bank.loans.enums.LoanConstantsEnum;
import com.bank.loans.exceptions.AlreadyExistsException;
import com.bank.loans.exceptions.NotFoundException;
import com.bank.loans.mapper.LoanMapper;
import com.bank.loans.repository.LoanRepository;
import com.bank.loans.service.ILoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class LoanServiceImpl implements ILoanService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoanServiceImpl.class);

    private final LoanRepository loanRepository;

    public LoanServiceImpl(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }


    @Override
    public void createLoan(String mobileNumber) {
        LOGGER.debug("createLoan method start");
        if (loanRepository.findByMobileNumber(mobileNumber).isPresent()) {
            throw new AlreadyExistsException("Loan already exists for mobile number: " + mobileNumber);
        }

        loanRepository.save(newLoan(mobileNumber));
        LOGGER.debug("createLoan method end");
    }

    private LoanEntity newLoan(String mobileNumber) {
        Long loanNumber = generateUniqueLoanNumber();
        return LoanEntity.builder()
                .loanNumber(loanNumber)
                .mobileNumber(mobileNumber)
                .loanType(LoanConstantsEnum.HOME_LOAN.getValue())
                .totalLoan(Integer.parseInt(LoanConstantsEnum.NEW_LOAN_LIMIT.getValue()))
                .amountPaid(0)
                .outstandingAmount(Integer.parseInt(LoanConstantsEnum.NEW_LOAN_LIMIT.getValue()))
                .build();
    }

    @Override
    public LoanDto fetchLoan(String mobileNumber) {
        LOGGER.debug("fetchLoan method start");
        LoanEntity loan = loanRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new NotFoundException("Loan not found for mobile number: " + mobileNumber)
        );
        LOGGER.debug("fetchLoan method end");
        return LoanMapper.mapToLoanDto(loan);
    }

    @Override
    public boolean updateLoan(LoanDto loanDto) {
        LOGGER.debug("updateLoan method start");
        LoanEntity loan = loanRepository.findByLoanNumber(Long.valueOf(loanDto.loanNumber())).orElseThrow(
                () -> new NotFoundException("Loan not found for loan number: " + loanDto.loanNumber()));
        LoanMapper.mapToLoanEntity(loanDto, loan);
        loanRepository.save(loan);
        LOGGER.debug("updateLoan method end");
        return true;
    }

    private Long generateUniqueLoanNumber() {
        long number;
        do {
            number = ThreadLocalRandom.current().nextLong(100_000_000_000L, 1_000_000_000_000L);
        } while (loanRepository.existsByLoanNumber(number));
        return number;
    }

    @Override
    public boolean deleteLoan(String mobileNumber) {
        LOGGER.debug("deleteLoan method start");
        LoanEntity loan = loanRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new NotFoundException("Loan not found for mobile number: " + mobileNumber)
        );
        loanRepository.deleteById(loan.getLoanId());
        LOGGER.debug("deleteLoan method end");
        return true;
    }

}
