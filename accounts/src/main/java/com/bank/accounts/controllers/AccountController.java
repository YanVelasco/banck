package com.bank.accounts.controllers;

import com.bank.accounts.dtos.AccountContactInfoDto;
import com.bank.accounts.dtos.CustomerDto;
import com.bank.accounts.dtos.ErrorResponseDto;
import com.bank.accounts.dtos.ResponseDto;
import com.bank.accounts.enums.AccountConstantsEnum;
import com.bank.accounts.services.IAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Account Management", description = "CRUD REST APIs for managing bank accounts and customer details")
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class AccountController {

    private final IAccountService accountService;
    private final AccountContactInfoDto accountContactInfoDto;

    @Value("${build.version}")
    private String buildVersion;

    private final Environment environment;

    public AccountController(IAccountService accountService, AccountContactInfoDto accountContactInfoDto, Environment environment) {
        this.accountService = accountService;
        this.accountContactInfoDto = accountContactInfoDto;
        this.environment = environment;
    }

    @Operation(
            summary = "Create a new bank account",
            description = "Creates a new customer and associated bank account. Returns 201 on success."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account created successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
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

    @Operation(
            summary = "Fetch account details",
            description = "Retrieves customer and account details by mobile number (10 digits)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account details fetched successfully",
                    content = @Content(schema = @Schema(implementation = CustomerDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid mobile number format",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Customer or account not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchAccountDetails(
            @Parameter(description = "Customer mobile number (exactly 10 digits)", example = "1234567890")
            @RequestParam @Valid @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits") String mobileNumber
    ) {
        CustomerDto customerDto = accountService.fetchAccountDetails(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(customerDto);
    }

    @Operation(
            summary = "Update account details",
            description = "Updates customer and account information. Returns 200 on success or 500 if the update fails."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account updated successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Customer or account not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Update failed due to an internal error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
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

    @Operation(
            summary = "Delete a bank account",
            description = "Deletes the customer and associated account by mobile number. Returns 204 on success."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Account deleted successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid mobile number format",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Customer or account not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Deletion failed due to an internal error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccount(
            @Parameter(description = "Customer mobile number (exactly 10 digits)", example = "1234567890")
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

    @Operation(
            summary = "Get build information",
            description = "Returns the build version of the application."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Build version retrieved successfully",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Failed to retrieve build version",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildVersion() {
        return ResponseEntity.status(HttpStatus.OK).body(buildVersion);
    }

    @Operation(
            summary = "Get Java version",
            description = "Returns the Java version used to run the application."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Java version retrieved successfully",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Failed to retrieve Java version",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion() {
        return ResponseEntity.status(HttpStatus.OK).body(environment.getProperty("JAVA_HOME"));
    }

    @Operation(
            summary = "Get account contact information",
            description = "Returns the contact information for the account."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account contact information retrieved successfully",
                    content = @Content(schema = @Schema(implementation = AccountContactInfoDto.class))),
            @ApiResponse(responseCode = "500", description = "Failed to retrieve account contact information",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/contact-info")
    public ResponseEntity<AccountContactInfoDto> getContactInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(accountContactInfoDto);
    }

}
