package com.bank.accounts.dtos.response;

import lombok.Builder;

@Builder
public record ResponseDto(
        String statusCode,
        String statusMessage
) {
}
