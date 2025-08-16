package com.gea.app.shared.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    // opsional: ambil dari application.yml -> app.api-base-url
    @Value("${app.api-base-url:}")
    private String apiBaseUrl;

    @Bean
    public OpenAPI apiInfo() {
        OpenAPI openApi = new OpenAPI()
                .info(new Info()
                        .title("API")
                        .version("v1")
                        .description("Dokumentasi API"))
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));

        // tambahkan servers hanya jika di-set (biar fleksibel antar environment)
        if (apiBaseUrl != null && !apiBaseUrl.isBlank()) {
            openApi.addServersItem(new Server().url(apiBaseUrl));
        }

        return openApi;
    }
}