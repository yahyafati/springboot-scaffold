package com.yahyafati.springbootauthenticationscaffold.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.yahyafati.springbootauthenticationscaffold.config.security.jwt.JWTAuthenticationFilter
import com.yahyafati.springbootauthenticationscaffold.config.security.jwt.JWTAuthorizationFilter
import com.yahyafati.springbootauthenticationscaffold.config.security.jwt.JWTService
import com.yahyafati.springbootauthenticationscaffold.models.auth.IUserServices
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val securityConfigProperties: SecurityConfigProperties,
    private val userService: IUserServices,
    private val jwtService: JWTService,
    private val mapper: ObjectMapper
) {

    companion object {

        private val LOG: Logger = LoggerFactory.getLogger(SecurityConfig::class.java)
    }


    init {
        LOG.info("SecurityConfig initialized")
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors(Customizer.withDefaults())
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilter(
                JWTAuthenticationFilter(authenticationManager(), securityConfigProperties, jwtService, mapper)
            )
            .addFilterAfter(
                JWTAuthorizationFilter(securityConfigProperties, userService, jwtService, mapper),
                JWTAuthenticationFilter::class.java
            )
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(
                        "/actuator/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                    ).permitAll()
                    .requestMatchers(
                        *securityConfigProperties.authEndpoints.toTypedArray()
                    ).permitAll()
                    .anyRequest().authenticated()

            }
        return http.build()
    }

    @Bean
    fun corsFilter(): CorsFilter {
        val corsProperties = securityConfigProperties.cors
        LOG.info("CORS Configuration: $corsProperties")
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowedOrigins = corsProperties.allowedOrigins
        config.allowedMethods = corsProperties.allowedMethods
        config.allowedHeaders = corsProperties.allowedHeaders
        config.addAllowedHeader(securityConfigProperties.jwt.header)
        config.allowCredentials = corsProperties.allowCredentials
        config.maxAge = corsProperties.maxAge
        config.exposedHeaders = listOf(
            *corsProperties.exposedHeaders.toTypedArray(),
            securityConfigProperties.jwt.header
        )
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }
}