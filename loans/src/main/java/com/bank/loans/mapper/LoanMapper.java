package com.bank.loans.mapper;

import com.bank.loans.dtos.LoanDto;
import com.bank.loans.entities.LoanEntity;

public class LoanMapper {

    public static LoanDto mapToLoanDto(LoanEntity loan) {
        return LoanDto.builder()
                .loanNumber(String.valueOf(loan.getLoanNumber()))
                .loanType(loan.getLoanType())
                .mobileNumber(loan.getMobileNumber())
                .totalLoan(loan.getTotalLoan())
                .amountPaid(loan.getAmountPaid())
                .outstandingAmount(loan.getOutstandingAmount())
                .build();
    }

    public static void mapToLoanEntity(LoanDto loanDto, LoanEntity loanEntity) {
        loanEntity.setLoanNumber(Long.valueOf(loanDto.loanNumber()));
        loanEntity.setLoanType(loanDto.loanType());
        loanEntity.setMobileNumber(loanDto.mobileNumber());
        loanEntity.setTotalLoan(loanDto.totalLoan());
        loanEntity.setAmountPaid(loanDto.amountPaid());
        loanEntity.setOutstandingAmount(loanDto.outstandingAmount());
    }
}
