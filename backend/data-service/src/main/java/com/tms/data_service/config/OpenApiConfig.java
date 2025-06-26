package com.tms.data_service.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";
    private static final String BASIC_SCHEME_NAME = "basicAuth";

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info()
                        .title("TMS-IOT Data Service API")
                        .description("API documentation for the TMS-IoT Data Service")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Leonardo Motta")
                                .email("leonardomottadev@gmail.com")
                        )
                )
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"))
                         .addSecuritySchemes(BASIC_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(BASIC_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic"))
                );
    }
}
