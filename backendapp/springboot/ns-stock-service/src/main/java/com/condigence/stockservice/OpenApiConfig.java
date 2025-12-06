package com.condigence.stockservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

/**
 * Central OpenAPI configuration for the NS Stock Service.
 *
 * With springdoc-openapi-starter-webmvc-ui on the classpath, this exposes:
 * - OpenAPI JSON at /v3/api-docs
 * - Swagger UI at /swagger-ui.html
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "NS Stock Service API",
                version = "0.0.1-SNAPSHOT",
                description = "Stock and inventory operations for NeerSeva platform.",
                contact = @Contact(
                        name = "NeerSeva Team",
                        email = "support@neerseva.local"
                ),
                license = @License(
                        name = "Proprietary",
                        url = "https://neerseva.local/license"
                )
        )
)
public class OpenApiConfig {
    // No explicit beans are required for basic configuration; the annotations
    // above are enough for springdoc to expose Swagger UI and OpenAPI metadata.
}

