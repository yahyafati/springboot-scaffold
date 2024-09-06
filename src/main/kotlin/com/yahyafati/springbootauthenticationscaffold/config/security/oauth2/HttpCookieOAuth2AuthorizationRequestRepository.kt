package com.yahyafati.springbootauthenticationscaffold.config.security.oauth2

import com.yahyafati.springbootauthenticationscaffold.utils.CookieUtils
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.stereotype.Component


/*
* By default, Spring OAuth2 uses HttpSessionOAuth2AuthorizationRequestRepository to save
* the authorization request. But, since our service is stateless, we can't save it in
* the session. We'll save the request in a Base64 encoded cookie instead.
*/
@Component
class HttpCookieOAuth2AuthorizationRequestRepository : AuthorizationRequestRepository<OAuth2AuthorizationRequest?> {

    init {
        LOG.info("HttpCookieOAuth2AuthorizationRequestRepository initialized")
    }


    override fun loadAuthorizationRequest(request: HttpServletRequest?): OAuth2AuthorizationRequest? {
        LOG.info("Loading authorization request from cookie")
        val cookie = CookieUtils.getCookie(request!!, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME) ?: return null

        val deserialized = CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest::class.java)

        return deserialized
    }

    override fun removeAuthorizationRequest(
        request: HttpServletRequest?,
        response: HttpServletResponse?
    ): OAuth2AuthorizationRequest? {
        return this.removeAuthorizationRequest(request)
    }

    override fun saveAuthorizationRequest(
        authorizationRequest: OAuth2AuthorizationRequest?,
        request: HttpServletRequest,
        response: HttpServletResponse?
    ) {
        if (authorizationRequest == null) {
            CookieUtils.deleteCookie(request, response!!, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
            CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME)
            return
        }

        CookieUtils.addCookie(
            response!!,
            OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
            CookieUtils.serialize(authorizationRequest),
            COOKIE_EXPIRE_SECONDS
        )

        val redirectUriAfterLogin: String = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME) ?: ""
        if (redirectUriAfterLogin.isNotBlank()) {
            CookieUtils.addCookie(
                response,
                REDIRECT_URI_PARAM_COOKIE_NAME,
                redirectUriAfterLogin,
                COOKIE_EXPIRE_SECONDS
            )
        }
    }

    fun removeAuthorizationRequest(request: HttpServletRequest?): OAuth2AuthorizationRequest? {
        return this.loadAuthorizationRequest(request)
    }

    fun removeAuthorizationRequestCookies(request: HttpServletRequest?, response: HttpServletResponse?) {
        CookieUtils.deleteCookie(request!!, response!!, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME)
    }

    companion object {

        const val OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME: String = "oauth2_auth_request"
        const val REDIRECT_URI_PARAM_COOKIE_NAME: String = "redirect_uri"
        private const val COOKIE_EXPIRE_SECONDS = 180

        private val LOG: Logger = LoggerFactory.getLogger(HttpCookieOAuth2AuthorizationRequestRepository::class.java)
    }
}