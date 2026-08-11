package com.bank.accounts.controllers;

import com.bank.accounts.dtos.CustomerDetailsDto;
import com.bank.accounts.dtos.CustomerDto;
import com.bank.accounts.dtos.ErrorResponseDto;
import com.bank.accounts.services.ICustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Customer Management", description = "REST APIs for fetching and managing customer details, accounts, loans, and cards")
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class CustomerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    private final ICustomerService customerService;

    public CustomerController(ICustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(
            summary = "Fetch Customer Details by Mobile Number",
            description = "Retrieves customer and account details by mobile number (10 digits)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer details fetched successfully",
                    content = @Content(schema = @Schema(implementation = CustomerDetailsDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid mobile number format",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Customer or account not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/customer/details")
    public ResponseEntity<CustomerDetailsDto> fetchCustomerDetails(
            @RequestHeader("bank-correlation-id") String correlationId,
            @Parameter(description = "Customer mobile number (exactly 10 digits)", example = "1234567890")
            @RequestParam @Valid @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits") String mobileNumber
    ) {
        LOGGER.debug("Correlation ID: {} - Fetching customer details for mobile number: {}", correlationId, mobileNumber);
        var customerDetails = customerService.getCustomerDetailsByMobileNumber(mobileNumber, correlationId);
        return ResponseEntity.status(HttpStatus.OK).body(customerDetails);
    }

}
