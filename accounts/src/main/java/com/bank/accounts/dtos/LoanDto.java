package com.bank.accounts.dtos;

import lombok.Builder;

@Builder
public record LoanDto(

        String mobileNumber,

        String loanNumber,

        String loanType,

        int totalLoan,

        int amountPaid,

        int outstandingAmount

) {
}
