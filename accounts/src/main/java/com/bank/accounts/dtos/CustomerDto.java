package com.bank.accounts.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Schema(name = "Customer", description = "Customer details along with associated bank account information")
@Builder
public record CustomerDto(

        @Schema(description = "Full name of the customer", example = "John Doe")
        @NotBlank(message = "Name cannot be empty")
        @Size(min = 5, max = 30, message = "Name must be between 5 and 30 characters")
        String name,

        @Schema(description = "Email address of the customer", example = "john.doe@example.com")
        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Email should be valid")
        String email,

        @Schema(description = "10-digit mobile number of the customer", example = "1234567890")
        @NotBlank(message = "Mobile number cannot be empty")
        @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits")
        String mobileNumber,

        @Schema(description = "Bank account details associated with the customer")
        AccountDto accountDto

) {
}
