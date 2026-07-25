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
                description = "API path invoked by client"
        )
        String apiPath,

        @Schema(
                description = "Error code representing the error happened"
        )
        HttpStatus errorCode,

        @Schema(
                description = "Error message representing the error happened"
        )
        String errorMessage,

        @Schema(
                description = "Time representing when the error happened"
        )
        LocalDateTime errorTime

) {
}
