package com.example.jwtvalidation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI jwtPracticalApi() {
        return new OpenAPI()
            .info(new Info()
                .title("JWT Validation and Claims Extraction API")
                .version("1.0")
                .description("Practical API to generate demo tokens and validate JWT signatures, expiry and claims."));
    }
}
