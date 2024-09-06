package com.yahyafati.springbootauthenticationscaffold.config.security.oauth2.userinfo

import com.yahyafati.springbootauthenticationscaffold.config.security.oauth2.CustomOAuth2User
import com.yahyafati.springbootauthenticationscaffold.models.auth.AuthUser

class GoogleOAuth2UserInfo(attributes: Map<String, Any>) : OAuth2UserInfo(attributes) {

    override fun toCustomOAuth2User(user: AuthUser): CustomOAuth2User {
        return CustomOAuth2User(user, nameKey)
    }

    override val nameKey: String
        get() = "sub"

    override val id: String
        get() = attributes[nameKey].toString()

    override val name: String
        get() = attributes["name"].toString()

    val familyName: String
        get() = attributes["family_name"].toString()

    override val email: String
        get() = attributes["email"].toString()

    val emailVerified: Boolean
        get() = attributes["email_verified"] as Boolean

    override val imageUrl: String
        get() = attributes["picture"].toString()
    override val firstName: String
        get() = attributes["given_name"].toString()
    override val lastName: String
        get() = attributes["family_name"].toString()

    override fun generateUsername(): String {
        val username = email.takeWhile { it != '@' }

        return if (username.length > 20) {
            username.substring(0, 20)
        } else {
            username
        }
    }
}