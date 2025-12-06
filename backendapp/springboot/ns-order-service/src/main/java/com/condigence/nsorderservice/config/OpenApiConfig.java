package com.condigence.nsorderservice.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI neerSevaOrderServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NeerSeva Order Service API")
                        .description("REST APIs for managing orders in the NeerSeva platform")
                        .version("v1")
                        .contact(new Contact()
                                .name("NeerSeva Team")
                                .email("support@neerseva.example"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("NeerSeva Order Service Documentation")
                        .url("https://neerseva.example/docs"));
    }
}

