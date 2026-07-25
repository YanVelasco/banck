package com.bank.accounts.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Bank Account Management API",
                version = "v1",
                description = "API for managing bank accounts and customer details",
                contact = @Contact(
                        name = "Your Name",
                        email = "your.email@example.com",
                        url = "https://www.example.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Bank Account Management API Documentation",
                url = "https://www.example.com/docs"
        )
)
public class OpenApiConfig {
}
