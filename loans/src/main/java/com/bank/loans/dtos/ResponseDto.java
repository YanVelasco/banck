package com.bank.loans.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(name = "Response", description = "Generic response returned on successful operations")
@Builder
public record ResponseDto(

        @Schema(description = "HTTP status code of the response", example = "201")
        String statusCode,

        @Schema(description = "Human-readable status message", example = "Loan created successfully")
        String statusMessage

) {
}
