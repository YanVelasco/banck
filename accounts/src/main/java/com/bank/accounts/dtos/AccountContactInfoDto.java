package com.bank.accounts.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@Schema(name = "AccountContactInfo", description = "Contact information for account-related inquiries")
@ConfigurationProperties(prefix = "accounts")
public record AccountContactInfoDto(

        @Schema(description = "Message to be displayed for account-related inquiries", example = "For any account-related inquiries, please contact our support team.")
        String message,

        @Schema(description = "Contact details for account-related inquiries", example = "{\"name\": \"Accounts Service\", \"email\": \"support@accounts.com\"}")
        Map<String, String> contactDetails,

        @Schema(description = "On-call support phone numbers", example = "[\"+1-800-123-4567\", \"+1-800-987-6543\"]")
        List<String> onCallSupport

) {
}
