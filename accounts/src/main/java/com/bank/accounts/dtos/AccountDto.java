package com.bank.accounts.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Schema(name = "Account", description = "Bank account information associated with a customer")
@Builder
public record AccountDto(

        @Schema(description = "Unique 10-digit account number", example = "1234567890")
        @Pattern(regexp = "\\d{10}", message = "Account number must be 10 digits")
        Long accountNumber,

        @Schema(description = "Type of bank account (e.g., savings, checking)", example = "savings")
        @NotBlank(message = "Account type cannot be empty")
        String accountType,

        @Schema(description = "Address of the branch where the account was opened", example = "123 Main Street, New York")
        @NotBlank(message = "Branch name cannot be empty")
        String branchAddress

) {
}
