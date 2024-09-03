package com.yahyafati.springbootauthenticationscaffold.config.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Claim
import com.auth0.jwt.interfaces.DecodedJWT
import com.yahyafati.springbootauthenticationscaffold.config.security.SecurityConfigProperties
import com.yahyafati.springbootauthenticationscaffold.models.auth.AuthUser
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*


@Service
class JWTService(securityConfigProperties: SecurityConfigProperties) {

    companion object {
        private val LOG = LoggerFactory.getLogger(JWTService::class.java)
    }

    init {
        LOG.info("JWTService initialized")
    }

    private val jwtProperties: SecurityConfigProperties.JwtProperties = securityConfigProperties.jwt
    private fun decodedJWT(token: String): DecodedJWT {
        return JWT.require(getSignInKey()).build().verify(token)
    }

    private fun extractAllClaims(token: String): Map<String, Claim> {
        return decodedJWT(token).claims
    }

    fun extractClaim(token: String, name: String): Claim? {
        return decodedJWT(token).getClaim(name)
    }

    fun generateToken(
        authUser: AuthUser, extraClaims: Map<String, Any> = emptyMap(),
    ): String {
        return buildToken(authUser, extraClaims)
    }

    private fun buildToken(
        authUser: AuthUser, extraClaims: Map<String, Any>
    ): String {
        val jwtBuilder = JWT.create()
        extraClaims.forEach { (key, value) -> jwtBuilder.withClaim(key, value.toString()) }

        return jwtBuilder
            .withSubject(authUser.username)
            .withIssuedAt(Instant.now())
            .withIssuer(jwtProperties.issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + jwtProperties.expiration))
            .sign(getSignInKey())
    }

    fun extractUsername(token: String): String {
        return decodedJWT(token).subject
    }

    private fun extractExpiration(token: String): Date {
        return decodedJWT(token).expiresAt
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    fun isTokenValid(token: String, authUserDetails: AuthUser): Boolean {
        val username: String = extractUsername(token)
        return username == authUserDetails.username && !isTokenExpired(token)
    }

    private fun getSignInKey(): Algorithm {
        return Algorithm.HMAC512(jwtProperties.secret.toByteArray())
    }
}