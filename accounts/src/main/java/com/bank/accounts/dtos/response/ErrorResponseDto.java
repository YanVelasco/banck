package com.bank.accounts.dtos.response;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        String apiPath,
        HttpStatus status,
        String errorMessage,
        LocalDateTime errorTime
) {
}
