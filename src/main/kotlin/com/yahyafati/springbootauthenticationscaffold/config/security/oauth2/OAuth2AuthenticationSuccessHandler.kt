package com.yahyafati.springbootauthenticationscaffold.config.security.oauth2

import com.yahyafati.springbootauthenticationscaffold.config.security.SecurityConfigProperties
import com.yahyafati.springbootauthenticationscaffold.config.security.jwt.JWTService
import com.yahyafati.springbootauthenticationscaffold.utils.CookieUtils
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class OAuth2AuthenticationSuccessHandler(
    private val httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository,
    private val jwtService: JWTService,
    securityConfigProperties: SecurityConfigProperties
) : SimpleUrlAuthenticationSuccessHandler() {

    companion object {

        val LOG: Logger = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler::class.java)
    }

    private val oAuth2Properties = securityConfigProperties.oauth2

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        val targetUrl = determineTargetUrl(request, response, authentication)

        if (response!!.isCommitted) {
            LOG.debug("Response has already been committed. Unable to redirect to $targetUrl")
            return
        }

        clearAuthenticationAttributes(request, response)
        redirectStrategy.sendRedirect(request, response, targetUrl)
    }

    override fun determineTargetUrl(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ): String {
//        val redirectUri = CookieUtils.getCookie(
//            request!!, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME
//        )?.value ?: throw IllegalArgumentException("Redirect URL not found")

        val redirectCookie = CookieUtils.getCookie(
            request!!, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME
        )

        val redirectUri = redirectCookie?.value ?: "/info"


        if (!isAuthorizedRedirectUri(redirectUri)) {
            throw IllegalArgumentException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication")
        }

        val targetUrl = redirectUri.ifEmpty { defaultTargetUrl }
        if (authentication?.principal is CustomOAuth2User) {
            val oAuth2User = authentication.principal as CustomOAuth2User
            val user = oAuth2User.user!!
            val token = jwtService.generateToken(user)
            val isNew = oAuth2User.newUser

            return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", token)
                .queryParam("new", isNew)
                .build()
                .toUriString()
        }
        return targetUrl

    }

    protected fun clearAuthenticationAttributes(request: HttpServletRequest?, response: HttpServletResponse?) {
        super.clearAuthenticationAttributes(request)
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)
    }

    private fun isAuthorizedRedirectUri(uri: String): Boolean {
        // TODO: Implement this method
        return true
//        val clientRedirectUri = URI.create(uri)
//
//        return oAuth2Properties.authorizedRedirectOrigins
//            .stream()
//            .anyMatch { authorizedRedirectOrigin ->
//                val authorizedURI = URI.create(authorizedRedirectOrigin)
//                authorizedURI.host.equals(clientRedirectUri.host, ignoreCase = true) &&
//                        authorizedURI.port == clientRedirectUri.port
//            }
    }
}