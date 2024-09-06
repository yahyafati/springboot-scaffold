package com.yahyafati.springbootauthenticationscaffold.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.yahyafati.springbootauthenticationscaffold.config.security.jwt.JWTAuthenticationFilter
import com.yahyafati.springbootauthenticationscaffold.config.security.jwt.JWTAuthorizationFilter
import com.yahyafati.springbootauthenticationscaffold.config.security.jwt.JWTService
import com.yahyafati.springbootauthenticationscaffold.config.security.oauth2.CustomOAuth2UserService
import com.yahyafati.springbootauthenticationscaffold.config.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository
import com.yahyafati.springbootauthenticationscaffold.config.security.oauth2.OAuth2AuthenticationSuccessHandler
import com.yahyafati.springbootauthenticationscaffold.models.auth.IAuthServices
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
    private val userService: IAuthServices,
    private val oauth2UserService: CustomOAuth2UserService,
    private val jwtService: JWTService,
    private val httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository,
    private val oAuth2AuthenticationSuccessHandler: OAuth2AuthenticationSuccessHandler,
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
        val permittedEndpoints = listOf(
            "/actuator/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/scalar-ui/**",
        )

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
                        *permittedEndpoints.toTypedArray(),
                        *securityConfigProperties.authEndpoints.toTypedArray(),
                        "/info/**", // TODO: Remove this only needed to show the OAuth2 user info
                    ).permitAll()
                    .requestMatchers("/oauth2/**", "/auth/**", "/oauth/**").permitAll()
                    .anyRequest().authenticated()

            }
            .oauth2Login { oauth2 ->
                oauth2
                    .authorizationEndpoint {
                        it
                            .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
                    }
                    .redirectionEndpoint {
                        it.baseUri("/oauth2/callback/*")
                    }
                    .userInfoEndpoint {
                        it.userService(oauth2UserService)
                    }
                    .successHandler(oAuth2AuthenticationSuccessHandler)
                    .failureHandler { _, _, exception ->
                        LOG.error("OAuth2 Login Failure: ${exception.message}")
                    }
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