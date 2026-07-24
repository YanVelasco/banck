package com.bank.accounts.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CustomerDto(

        @NotBlank(message = "Name cannot be empty")
        @Size(min = 5, max = 30, message = "Name must be between 5 and 30 characters")
        String name,

        @Email(message = "Email should be valid")
        String email,

        @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits")
        String mobileNumber,

        AccountDto accountDto

) {
}
