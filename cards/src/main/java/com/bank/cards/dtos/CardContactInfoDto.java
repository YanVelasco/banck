package com.bank.cards.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@Schema(name = "CardContactInfo", description = "Contact information for card-related inquiries")
@ConfigurationProperties(prefix = "cards")
@Getter
@Setter
public class CardContactInfoDto {

    @Schema(description = "Message to be displayed for card-related inquiries", example = "For any card-related inquiries, please contact our support team.")
    private String message;

    @Schema(description = "Contact details for card-related inquiries", example = "{\"name\": \"Cards Service\", \"email\": \"support@cards.com\"}")
    private Map<String, String> contactDetails;

    @Schema(description = "On-call support phone numbers", example = "[\"+1-800-123-4567\", \"+1-800-987-6543\"]")
    private List<String> onCallSupport;

}
