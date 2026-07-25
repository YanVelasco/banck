package com.bank.loans.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Schema(name = "ErrorResponse", description = "Schema returned when an error occurs during request processing")
public record ErrorResponseDto(

        @Schema(description = "The API path that triggered the error", example = "/api/fetch")
        String apiPath,

        @Schema(description = "HTTP status of the error response", example = "404")
        HttpStatus status,

        @Schema(description = "Human-readable error message", example = "Loan not found with the given mobile number")
        String errorMessage,

        @Schema(description = "Timestamp of when the error occurred", example = "2024-01-15T10:30:00")
        LocalDateTime errorTime

) {
}
