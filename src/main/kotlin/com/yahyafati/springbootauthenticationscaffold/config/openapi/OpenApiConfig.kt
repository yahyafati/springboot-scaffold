package com.yahyafati.springbootauthenticationscaffold.config.openapi

import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    
    @Bean
    fun openApiCustomizer(): OpenApiCustomizer {
        return OpenApiCustomizer { openApi ->
            openApi.components.addSecuritySchemes(
                "token",
                SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
            )
        }
    }
}