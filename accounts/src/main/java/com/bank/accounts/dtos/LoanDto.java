package com.bank.accounts.dtos;

public record LoanDto(

        String mobileNumber,

        String loanNumber,

        String loanType,

        int totalLoan,

        int amountPaid,

        int outstandingAmount

) {
}
