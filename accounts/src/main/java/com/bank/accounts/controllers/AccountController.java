package com.bank.accounts.controllers;

import com.bank.accounts.enums.AccountConstantsEnum;
import com.bank.accounts.dtos.CustomerDto;
import com.bank.accounts.dtos.response.ResponseDto;
import com.bank.accounts.services.IAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AccountController {

    private final IAccountService accountService;

    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(
            @RequestBody CustomerDto customerDto
    ) {
        accountService.createAccount(customerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseDto.builder()
                        .statusCode(AccountConstantsEnum.STATUS_201.getValue())
                        .statusMessage(AccountConstantsEnum.MESSAGE_201.getValue())
                        .build()
        );
    }

}
