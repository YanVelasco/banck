package com.bank.accounts.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record AccountDto(

        @Pattern(regexp = "\\d{10}", message = "Account number must be 10 digits")
        Long accountNumber,

        @NotBlank(message = "Account type cannot be empty")
        String accountType,

        @NotBlank(message = "Branch name cannot be empty")
        String branchAddress

) {
}
