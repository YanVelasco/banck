package com.bank.accounts.controllers;

import com.bank.accounts.dtos.CustomerDto;
import com.bank.accounts.dtos.ResponseDto;
import com.bank.accounts.enums.AccountConstantsEnum;
import com.bank.accounts.services.IAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchAccountDetails(
            @RequestParam String mobileNumber
    ) {
        CustomerDto customerDto = accountService.fetchAccountDetails(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(customerDto);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccountDetails(
            @RequestBody CustomerDto customerDto
    ) {
        boolean isUpdated = accountService.updateAccountDetails(customerDto);
        if (isUpdated) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    ResponseDto.builder()
                            .statusCode(AccountConstantsEnum.STATUS_200.getValue())
                            .statusMessage(AccountConstantsEnum.MESSAGE_200.getValue())
                            .build()
            );
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ResponseDto.builder()
                            .statusCode(AccountConstantsEnum.STATUS_500.getValue())
                            .statusMessage(AccountConstantsEnum.MESSAGE_500.getValue())
                            .build()
            );
        }
    }


}
