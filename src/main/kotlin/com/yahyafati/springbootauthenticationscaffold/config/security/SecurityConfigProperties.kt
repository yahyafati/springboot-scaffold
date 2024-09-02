package com.yahyafati.springbootauthenticationscaffold.config.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "com.yahyafati.scaffold")
data class SecurityConfigProperties(
    var jwt: JwtProperties = JwtProperties(),
    var cors: CorsProperties = CorsProperties(),
    var baseURL: String = "/api/v1/auth",
) {

    final var loginUrl: String = "/login"
        get() = "$baseURL$field"

    private final var logoutUrl: String = "/logout"
        get() = "$baseURL$field"

    private final var registerUrl: String = "/register"
        get() = "$baseURL$field"

    final val authEndpoints: List<String> = listOf(loginUrl, logoutUrl, registerUrl)

    data class JwtProperties(
        var secret: String = "",
        var expiration: Long = 0L,
        var header: String = "",
        var prefix: String = "",
        var issuer: String = "self",
    )

    data class CorsProperties(
        var allowedOrigins: List<String> = listOf("*"),
        var allowedMethods: List<String> = listOf("GET", "POST", "PUT", "DELETE"),
        var allowedHeaders: List<String> = listOf("*"),
        var exposedHeaders: List<String> = listOf("Authorization"),
        var allowCredentials: Boolean = false,
        var maxAge: Long = 3600L,
    )

}