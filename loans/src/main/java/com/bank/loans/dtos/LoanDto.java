package com.bank.loans.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(name = "LoanDto", description = "Data transfer object for loan information")
public record LoanDto(

        @Schema(description = "10-digit mobile number of the customer", example = "1234567890")
        @NotBlank(message = "Mobile number cannot be empty")
        @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits")
        String mobileNumber,

        @Schema(description = "Unique loan number with 12 digits", example = "123456789012")
        @NotBlank(message = "Loan number cannot be empty")
        @Pattern(regexp = "\\d{12}", message = "Loan number must be 12 digits")
        String loanNumber,

        @NotBlank(message = "Loan type cannot be null or empty")
        @Schema(description = "Type of loan (e.g., Home Loan, Personal Loan, Auto Loan)", example = "Home Loan")
        String loanType,

        @Positive(message = "Total loan amount must be zero or positive")
        @Schema(description = "Total loan amount", example = "50000")
        int totalLoan,

        @PositiveOrZero(message = "Outstanding amount must be zero or positive")
        @Schema(description = "Outstanding loan amount", example = "20000")
        int outstandingAmount

) {
}
