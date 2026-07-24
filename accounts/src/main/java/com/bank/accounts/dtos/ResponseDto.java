package com.bank.accounts.dtos;

import lombok.Builder;

@Builder
public record ResponseDto(
        String statusCode,
        String statusMessage
) {
}
