package com.condigence.imageservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * Central OpenAPI / Swagger configuration for the Image Service.
 *
 * The Springdoc starter will automatically expose:
 * - OpenAPI JSON at /v3/api-docs
 * - Swagger UI at /swagger-ui.html (or /swagger-ui)
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "NeerSeva Image Service API",
                version = "v1",
                description = "REST API for managing and serving images in the NeerSeva platform.",
                contact = @Contact(
                        name = "NeerSeva Team",
                        email = "support@neerseva.example"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local development")
        }
)
public class OpenApiConfig {

    // No implementation needed; annotations drive the OpenAPI metadata.

}

