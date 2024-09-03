package com.yahyafati.springbootauthenticationscaffold.config.security.jwt

import com.auth0.jwt.exceptions.JWTVerificationException
import com.fasterxml.jackson.databind.ObjectMapper
import com.yahyafati.springbootauthenticationscaffold.config.security.SecurityConfigProperties
import com.yahyafati.springbootauthenticationscaffold.exceptions.base.BaseErrorModel
import com.yahyafati.springbootauthenticationscaffold.exceptions.base.ExceptionType
import com.yahyafati.springbootauthenticationscaffold.models.auth.AuthUser
import com.yahyafati.springbootauthenticationscaffold.models.auth.IAuthServices
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.filter.OncePerRequestFilter

class JWTAuthorizationFilter(
    securityConfigProperties: SecurityConfigProperties,
    private val userService: IAuthServices,
    private val jwtService: JWTService,
    private val mapper: ObjectMapper
) : OncePerRequestFilter() {

    private var jwtProperties: SecurityConfigProperties.JwtProperties = securityConfigProperties.jwt

    companion object {

        private val LOG = LoggerFactory.getLogger(JWTAuthorizationFilter::class.java)
    }

    init {
        LOG.info("JWTAuthorizationFilter initialized")
    }

    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain
    ) {
        val header = request.getHeader(jwtProperties.header)
        if (header == null || !header.startsWith(jwtProperties.prefix)) {
            LOG.debug("No JWT token found in request headers")
            chain.doFilter(request, response)
            return
        }

        val authentication = try {
            getAuthentication(request)
        } catch (ex: JWTVerificationException) {
            respondUnauthorized(response, ex)
            return
        } catch (ex: UsernameNotFoundException) {
            respondUnauthorized(response, ex)
            return
        }

        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(request, response)
    }

    private fun respondUnauthorized(response: HttpServletResponse, ex: Exception) {
        val errorModel = BaseErrorModel(
            type = ExceptionType.UNAUTHORIZED,
            message = ex.message ?: "Unauthorized",
            status = HttpStatus.UNAUTHORIZED.value(),
        )
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpStatus.UNAUTHORIZED.value()
        val outputStream = response.outputStream
        mapper.writeValue(outputStream, errorModel)
        outputStream.flush()
    }

    private fun getAuthentication(request: HttpServletRequest): UsernamePasswordAuthenticationToken? {
        val rawToken = request.getHeader(jwtProperties.header) ?: return null

        try {
            val token = rawToken.replace(jwtProperties.prefix, "")
            val username: String = jwtService.extractUsername(token)
            val loggedInAuthUser: AuthUser = userService.findByUsername(username)
                ?: throw UsernameNotFoundException("User with username $username not found")
            return UsernamePasswordAuthenticationToken(
                loggedInAuthUser,
                null,
                loggedInAuthUser.authorities
            )
        } catch (ex: JWTVerificationException) {
            LOG.error(ex.message)
            throw ex
        } catch (ex: UsernameNotFoundException) {
            LOG.error(ex.message)
            throw ex
        }
    }
}