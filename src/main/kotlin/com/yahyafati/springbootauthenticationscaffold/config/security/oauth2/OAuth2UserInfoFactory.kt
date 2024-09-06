package com.yahyafati.springbootauthenticationscaffold.config.security.oauth2

import com.yahyafati.springbootauthenticationscaffold.config.security.oauth2.userinfo.GitHubOAuth2UserInfo
import com.yahyafati.springbootauthenticationscaffold.config.security.oauth2.userinfo.GoogleOAuth2UserInfo
import com.yahyafati.springbootauthenticationscaffold.config.security.oauth2.userinfo.OAuth2UserInfo
import com.yahyafati.springbootauthenticationscaffold.models.auth.provider.EOAuth2Provider

object OAuth2UserInfoFactory {

    fun getOAuth2UserInfo(registrationId: String, attributes: Map<String, Any>): OAuth2UserInfo {
        return if (registrationId.equals(EOAuth2Provider.GOOGLE.toString(), ignoreCase = true)) {
            GoogleOAuth2UserInfo(attributes)
        } else if (registrationId.equals(EOAuth2Provider.GITHUB.toString(), ignoreCase = true)) {
            return GitHubOAuth2UserInfo(attributes)
        } else {
            throw OAuth2AuthenticationProcessingException("Sorry! Login with $registrationId is not supported yet.")
        }
    }
}