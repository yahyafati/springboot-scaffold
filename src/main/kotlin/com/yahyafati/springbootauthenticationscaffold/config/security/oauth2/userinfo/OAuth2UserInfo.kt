package com.yahyafati.springbootauthenticationscaffold.config.security.oauth2.userinfo

import com.yahyafati.springbootauthenticationscaffold.config.security.oauth2.CustomOAuth2User
import com.yahyafati.springbootauthenticationscaffold.models.auth.AuthUser

abstract class OAuth2UserInfo(var attributes: Map<String, Any>) {

    abstract fun toCustomOAuth2User(user: AuthUser): CustomOAuth2User

    abstract val nameKey: String

    abstract val id: String

    abstract val name: String

    abstract val email: String

    abstract val imageUrl: String
    abstract val firstName: String
    abstract val lastName: String

    abstract fun generateUsername(): String
}
