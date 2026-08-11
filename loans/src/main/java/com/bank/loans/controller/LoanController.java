package com.bank.loans.controller;

import com.bank.loans.dtos.ErrorResponseDto;
import com.bank.loans.dtos.LoanDto;
import com.bank.loans.dtos.LoansContactInfoDto;
import com.bank.loans.dtos.ResponseDto;
import com.bank.loans.enums.LoanConstantsEnum;
import com.bank.loans.service.ILoanService;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Loan Management", description = "CRUD REST APIs for managing loans and customer details")
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class LoanController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoanController.class);

    private final ILoanService iloanService;
    private final LoansContactInfoDto loansContactInfoDto;

    @Value("${build.version}")
    private String buildVersion;

    private final Environment environment;

    public LoanController(ILoanService iloanService, LoansContactInfoDto loansContactInfoDto, Environment environment) {
        this.iloanService = iloanService;
        this.loansContactInfoDto = loansContactInfoDto;
        this.environment = environment;
    }

    @Operation(
            summary = "Create a new loan",
            description = "Creates a new loan for the customer identified by the given mobile number. Returns 201 on success."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Loan created successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid mobile number format",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Loan already exists for this mobile number",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createLoan(
            @Parameter(description = "Customer mobile number (exactly 10 digits)", example = "1234567890")
            @RequestParam
            @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
            String mobileNumber) {
        iloanService.createLoan(mobileNumber);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ResponseDto.builder()
                                .statusCode("201")
                                .statusMessage("Loan created successfully")
                                .build()
                );
    }

    @Operation(
            summary = "Fetch loan details",
            description = "Retrieves loan details for the customer identified by the given mobile number."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Loan details fetched successfully",
                    content = @Content(schema = @Schema(implementation = LoanDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid mobile number format",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Loan not found for the given mobile number",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/fetch")
    public ResponseEntity<LoanDto> fetchLoanDetails(
            @RequestHeader("bank-correlation-id") String correlationId,
            @Parameter(description = "Customer mobile number (exactly 10 digits)", example = "1234567890")
            @RequestParam
            @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
            String mobileNumber) {
        LOGGER.debug("Correlation id found: {}", correlationId);
        LOGGER.debug("Fetching loan details for mobile number: {}", mobileNumber);
        LoanDto loanDto = iloanService.fetchLoan(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(loanDto);
    }

    @Operation(
            summary = "Update loan details",
            description = "Updates loan information for an existing customer. Returns 200 on success or 417 if the update fails."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Loan updated successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Loan not found for the given mobile number",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "417", description = "Update operation failed",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateLoanDetails(@Valid @RequestBody LoanDto loanDto) {
        boolean isUpdated = iloanService.updateLoan(loanDto);
        if (isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(LoanConstantsEnum.STATUS_200.getValue(), LoanConstantsEnum.MESSAGE_200.getValue()));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(LoanConstantsEnum.STATUS_417.getValue(), LoanConstantsEnum.MESSAGE_417_UPDATE.getValue()));
        }
    }

    @Operation(
            summary = "Delete a loan",
            description = "Deletes the loan associated with the given mobile number. Returns 200 on success or 417 if the deletion fails."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Loan deleted successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid mobile number format",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Loan not found for the given mobile number",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "417", description = "Delete operation failed",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteLoanDetails(
            @Parameter(description = "Customer mobile number (exactly 10 digits)", example = "1234567890")
            @RequestParam
            @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
            String mobileNumber) {
        boolean isDeleted = iloanService.deleteLoan(mobileNumber);
        if (isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(LoanConstantsEnum.STATUS_200.getValue(), LoanConstantsEnum.MESSAGE_200.getValue()));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(LoanConstantsEnum.STATUS_417.getValue(), LoanConstantsEnum.MESSAGE_417_DELETE.getValue()));
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
            summary = "Get loans contact information",
            description = "Returns the contact information for the loans."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Loans contact information retrieved successfully",
                    content = @Content(schema = @Schema(implementation = LoansContactInfoDto.class))),
            @ApiResponse(responseCode = "500", description = "Failed to retrieve loans contact information",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/contact-info")
    public ResponseEntity<LoansContactInfoDto> getContactInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(loansContactInfoDto);
    }

}
