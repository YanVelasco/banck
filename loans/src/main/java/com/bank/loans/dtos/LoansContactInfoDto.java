package com.bank.loans.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@Schema(name = "LoansContactInfo", description = "Contact information for loans-related inquiries")
@ConfigurationProperties(prefix = "loans")
public record LoansContactInfoDto(

        @Schema(description = "Message to be displayed for loans-related inquiries", example = "For any loans-related inquiries, please contact our support team.")
        String message,

        @Schema(description = "Contact details for loans-related inquiries", example = "{\"name\": \"Loans Service\", \"email\": \"support@loans.com\"}")
        Map<String, String> contactDetails,

        @Schema(description = "On-call support phone numbers", example = "[\"+1-800-123-4567\", \"+1-800-987-6543\"]")
        List<String> onCallSupport

) {
}
