package com.bank.cards.controller;

import com.bank.cards.dtos.CardContactInfoDto;
import com.bank.cards.dtos.CardDto;
import com.bank.cards.dtos.ErrorResponseDto;
import com.bank.cards.dtos.ResponseDto;
import com.bank.cards.enums.CardsConstantsEnum;
import com.bank.cards.service.ICardService;
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

@Tag(name = "Card Management", description = "CRUD REST APIs for managing cards and customer details")
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class CardController {

    private final ICardService iCardService;
    private final CardContactInfoDto cardContactInfoDto;
    private static final Logger LOGGER = LoggerFactory.getLogger(CardController.class);

    @Value("${build.version}")
    private String buildVersion;

    private final Environment environment;

    public CardController(ICardService iCardService, CardContactInfoDto cardContactInfoDto, Environment environment) {
        this.iCardService = iCardService;
        this.cardContactInfoDto = cardContactInfoDto;
        this.environment = environment;
    }

    @Operation(
            summary = "Create a new card",
            description = "Creates a new credit card for the customer identified by the given mobile number. Returns 201 on success."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Card created successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid mobile number format",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Card already exists for this mobile number",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createCard(
            @Parameter(description = "Customer mobile number (exactly 10 digits)", example = "1234567890")
            @Valid @RequestParam
            @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
            String mobileNumber) {
        LOGGER.debug("createCard method start");
        iCardService.createCard(mobileNumber);
        LOGGER.debug("createCard method end");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ResponseDto.builder()
                                .statusCode(CardsConstantsEnum.STATUS_201.getValue())
                                .statusMsg(CardsConstantsEnum.MESSAGE_201.getValue())
                                .build()
                );
    }

    @Operation(
            summary = "Fetch card details",
            description = "Retrieves card details for the customer identified by the given mobile number."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Card details fetched successfully",
                    content = @Content(schema = @Schema(implementation = CardDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid mobile number format",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Card not found for the given mobile number",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/fetch")
    public ResponseEntity<CardDto> fetchCardDetails(
            @RequestHeader("bank-correlation-id") String correlationId,
            @Parameter(description = "Customer mobile number (exactly 10 digits)", example = "1234567890")
            @RequestParam
            @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
            String mobileNumber
    ) {
        LOGGER.debug("fetchCardDetails method start");
        LOGGER.debug("Correlation id found: {}", correlationId);
        CardDto cardDto = iCardService.fetchCard(mobileNumber);
        LOGGER.debug("fetchCardDetails method end");
        return ResponseEntity.status(HttpStatus.OK).body(cardDto);
    }

    @Operation(
            summary = "Update card details",
            description = "Updates card information for an existing customer. Returns 200 on success or 417 if the update fails."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Card updated successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Card not found for the given card number",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "417", description = "Update operation failed",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateCardDetails(@Valid @RequestBody CardDto cardsDto) {
        LOGGER.debug("updateCardDetails method start");
        boolean isUpdated = iCardService.updateCard(cardsDto);
        LOGGER.debug("updateCardDetails method end");
        if (isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            ResponseDto.builder()
                                    .statusCode(CardsConstantsEnum.STATUS_200.getValue())
                                    .statusMsg(CardsConstantsEnum.MESSAGE_200.getValue())
                                    .build()
                    );
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(
                            ResponseDto.builder()
                                    .statusCode(CardsConstantsEnum.STATUS_417.getValue())
                                    .statusMsg(CardsConstantsEnum.MESSAGE_417_UPDATE.getValue())
                                    .build()
                    );
        }
    }

    @Operation(
            summary = "Delete a card",
            description = "Deletes the card associated with the given mobile number. Returns 200 on success or 417 if the deletion fails."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Card deleted successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid mobile number format",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Card not found for the given mobile number",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "417", description = "Delete operation failed",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteCardDetails(
            @Parameter(description = "Customer mobile number (exactly 10 digits)", example = "1234567890")
            @RequestParam
            @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
            String mobileNumber) {
        LOGGER.debug("deleteCardDetails method start");
        boolean isDeleted = iCardService.deleteCard(mobileNumber);
        LOGGER.debug("deleteCardDetails method end");
        if (isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            ResponseDto.builder()
                                    .statusCode(CardsConstantsEnum.STATUS_200.getValue())
                                    .statusMsg(CardsConstantsEnum.MESSAGE_200.getValue())
                                    .build()
                    );
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(
                            ResponseDto.builder()
                                    .statusCode(CardsConstantsEnum.STATUS_417.getValue())
                                    .statusMsg(CardsConstantsEnum.MESSAGE_417_DELETE.getValue())
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
        LOGGER.debug("getBuildVersion method start");
        var response = ResponseEntity.status(HttpStatus.OK).body(buildVersion);
        LOGGER.debug("getBuildVersion method end");
        return response;
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
        LOGGER.debug("getJavaVersion method start");
        var response = ResponseEntity.status(HttpStatus.OK).body(environment.getProperty("JAVA_HOME"));
        LOGGER.debug("getJavaVersion method end");
        return response;
    }

    @Operation(
            summary = "Get card contact information",
            description = "Returns the contact information for the card."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Card contact information retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CardContactInfoDto.class))),
            @ApiResponse(responseCode = "500", description = "Failed to retrieve card contact information",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/contact-info")
    public ResponseEntity<CardContactInfoDto> getContactInfo() {
        LOGGER.debug("getContactInfo method start");
        var response = ResponseEntity.status(HttpStatus.OK).body(cardContactInfoDto);
        LOGGER.debug("getContactInfo method end");
        return response;
    }

}