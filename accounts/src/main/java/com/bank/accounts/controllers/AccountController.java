package com.bank.accounts.controllers;

import com.bank.accounts.dtos.CustomerDto;
import com.bank.accounts.dtos.ResponseDto;
import com.bank.accounts.enums.AccountConstantsEnum;
import com.bank.accounts.services.IAccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class AccountController {

    private final IAccountService accountService;

    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(
            @RequestBody @Valid CustomerDto customerDto
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
            @RequestParam @Valid @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits") String mobileNumber
    ) {
        CustomerDto customerDto = accountService.fetchAccountDetails(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(customerDto);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccountDetails(
            @RequestBody @Valid CustomerDto customerDto
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

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccount(
            @RequestParam @Valid @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits") String mobileNumber
    ) {
        boolean isDeleted = accountService.deleteAccount(mobileNumber);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                    ResponseDto.builder()
                            .statusCode(AccountConstantsEnum.STATUS_204.getValue())
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
