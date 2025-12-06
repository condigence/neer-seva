package com.condigence.nsproductservice.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI nsProductServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NS Product Service API")
                        .description("NeerSeva Product Service REST API documentation")
                        .version("v0.0.1")
                        .contact(new Contact()
                                .name("NeerSeva Support")
                                .email("support@neerseva.local"))
                        .license(new License().name("Proprietary")))
                .externalDocs(new ExternalDocumentation()
                        .description("Project Repository")
                        .url("https://example.com/neer-seva"));
    }

    @Bean
    public GroupedOpenApi productPublicApi() {
        return GroupedOpenApi.builder()
                .group("product-service")
                .packagesToScan("com.condigence.nsproductservice.controller")
                .build();
    }
}
