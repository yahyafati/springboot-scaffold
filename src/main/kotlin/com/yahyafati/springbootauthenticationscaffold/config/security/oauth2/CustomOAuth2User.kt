package com.yahyafati.springbootauthenticationscaffold.config.security.oauth2

import com.yahyafati.springbootauthenticationscaffold.models.auth.AuthUser
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User

class CustomOAuth2User(
    private val oAuth2User: OAuth2User,
) : OAuth2User {

    var user: AuthUser? = null
    var newUser: Boolean = false

    constructor(user: AuthUser, nameKey: String) : this(
        DefaultOAuth2User(
            user.getAuthorities(),
            mapOf(nameKey to user.id),
            nameKey
        )
    ) {
        this.user = user
    }

    override fun getName(): String {
        return oAuth2User.name
    }

    fun getEmail(): String {
        return oAuth2User.attributes["email"] as String
    }

    override fun getAttributes(): MutableMap<String, Any> {
        return oAuth2User.attributes
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return oAuth2User.authorities
    }

    override fun toString(): String {
        return "CustomOAuth2User(oAuth2User=$oAuth2User)"
    }

}
