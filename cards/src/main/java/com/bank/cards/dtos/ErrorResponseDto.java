package com.bank.cards.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Schema(
        name = "ErrorResponse",
        description = "Schema returned when an error occurs during request processing"
)
@Builder
public record ErrorResponseDto(

        @Schema(
                description = "API path invoked by client",
                example = "/api/fetch"
        )
        String apiPath,

        @Schema(
                description = "HTTP status code representing the error",
                example = "404"
        )
        HttpStatus errorCode,

        @Schema(
                description = "Error message describing what went wrong",
                example = "Card not found with the given mobile number"
        )
        String errorMessage,

        @Schema(
                description = "Timestamp of when the error occurred",
                example = "2024-01-15T10:30:00"
        )
        LocalDateTime errorTime

) {
}
