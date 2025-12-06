package com.condigence.nsuserservice.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI neerSevaUserServiceOpenAPI() {
        OpenAPI openAPI = new OpenAPI();
        openAPI.setExternalDocs(new ExternalDocumentation()
                .description("Neer Seva User Service Documentation")
                .url("https://example.com/neerseva/user-service-docs"));
        return openAPI;
    }
}
