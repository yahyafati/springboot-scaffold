package com.yahyafati.springbootauthenticationscaffold.config.openapi

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.IOException


@OpenAPIDefinition
@Configuration
@SecurityScheme(
    name = "token",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer",
    description = "JWT Token for authentication (Token only, no Bearer)"
)
class SwaggerConfig {

    @Bean
    @Throws(IOException::class)
    fun baseOpenAPI(): OpenAPI {
        val api = OpenAPI()
            .info(
                Info()
                    .title("Spring Boot Authentication Scaffold API")
                    .version("1.0.0")
                    .description("API for managing users and authentication in a Spring Boot application.")
                    .summary("Spring Boot Authentication Scaffold API")
            )

        api.security = listOf(
            SecurityRequirement().addList("token")
        )
        return api
    }

}