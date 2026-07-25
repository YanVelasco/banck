package com.bank.cards.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(
        name = "ResponseDto",
        description = "Schema returned when a request is processed successfully"
)
@Builder
public record ResponseDto(

        @Schema(
                description = "Status code in the response",
                example = "201"
        )
        String statusCode,

        @Schema(
                description = "Status message in the response",
                example = "Card created successfully"
        )
        String statusMsg

) {
}
