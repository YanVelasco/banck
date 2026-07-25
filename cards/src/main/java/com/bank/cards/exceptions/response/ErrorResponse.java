package com.bank.cards.exceptions.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Schema(name = "ErrorResponse", description = "Detailed error response returned when an exception is handled by the global handler")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(

        @Schema(description = "HTTP status code", example = "404")
        int status,

        @Schema(description = "Short error title", example = "Not Found")
        String error,

        @Schema(description = "Detailed error message", example = "Customer not found with the given mobile number")
        String message,

        @Schema(description = "Request path that caused the error", example = "/api/fetch")
        String path,

        @Schema(description = "ISO-8601 timestamp of the error", example = "2024-01-15T10:30:00")
        String timestamp,

        @Schema(description = "Field-level validation error details (only present on validation failures)")
        Map<String, String> details

) {

    public ErrorResponse(int status, String error, String message, String path) {
        this(status, error, message, path, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), null);
    }

    public ErrorResponse(int value, String validationError, String message, Map<String, String> errors) {
        this(value, validationError, message, null, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                , errors);
    }


    public static ErrorResponse of(int status, String error, String message, String path, Map<String, String> details) {
        return new ErrorResponse(status, error, message, path,
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), details);
    }
}
