package com.yahyafati.springbootauthenticationscaffold.config.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.yahyafati.springbootauthenticationscaffold.config.security.SecurityConfigProperties
import com.yahyafati.springbootauthenticationscaffold.models.auth.AuthUser
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
import java.util.*

open class JWTAuthenticationFilter(
    private val authenticationManager: AuthenticationManager,
    securityConfigProperties: SecurityConfigProperties,
    private val jwtService: JWTService,
    private var objectMapper: ObjectMapper,
) : UsernamePasswordAuthenticationFilter() {

    companion object {

        private val LOG = LoggerFactory.getLogger(JWTAuthenticationFilter::class.java)

        data class AccountCredentials(
            var username: String = "",
            var password: String = "",
        )
    }

    private val jwtProperties: SecurityConfigProperties.JwtProperties = securityConfigProperties.jwt

    init {
        LOG.info("JWTAuthenticationFilter initialized")
        LOG.info("Setting login URL to ${securityConfigProperties.loginUrl}")
        this.setFilterProcessesUrl(securityConfigProperties.loginUrl)
    }

    @Throws(AuthenticationException::class)
    @Transactional(readOnly = true, rollbackFor = [Exception::class], propagation = Propagation.REQUIRED)
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val credentials = ObjectMapper().readValue(request.inputStream, AccountCredentials::class.java)
        val authenticationToken = UsernamePasswordAuthenticationToken(
            credentials.username,
            credentials.password,
            listOf<GrantedAuthority>()
        )
        return authenticationManager.authenticate(authenticationToken)
    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(
        request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, authResult: Authentication
    ) {
        val token = jwtService.generateToken(authResult.principal as AuthUser)
        response.addHeader(jwtProperties.header, jwtProperties.prefix + token)
        val responseData = mapOf(
            "title" to "Authentication successful",
            "token" to token,
            "status" to HttpServletResponse.SC_OK,
        )
        val responseBody = this.objectMapper.writeValueAsString(responseData)
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.writer?.write(responseBody)
    }

    @Throws(IOException::class, ServletException::class)
    override fun unsuccessfulAuthentication(
        request: HttpServletRequest, response: HttpServletResponse, failed: AuthenticationException
    ) {
        LOG.error("Unsuccessful Authentication")
        val responseData = mapOf(
            "title" to "Authentication failed",
            "message" to failed.message,
            "timestamp" to Date().time,
            "status" to HttpServletResponse.SC_UNAUTHORIZED,
        )
        val responseBody = this.objectMapper.writeValueAsString(responseData)

        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.writer?.write(responseBody)
        response.status = HttpServletResponse.SC_UNAUTHORIZED
    }

}