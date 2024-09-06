package com.yahyafati.springbootauthenticationscaffold.config.security.oauth2.userinfo

import com.yahyafati.springbootauthenticationscaffold.config.security.oauth2.CustomOAuth2User
import com.yahyafati.springbootauthenticationscaffold.models.auth.AuthUser

class GitHubOAuth2UserInfo(attributes: Map<String, Any>) : OAuth2UserInfo(attributes) {
    override fun toCustomOAuth2User(user: AuthUser): CustomOAuth2User {
        return CustomOAuth2User(user, nameKey)
    }

    override val nameKey: String
        get() = "id"
    override val id: String
        get() = attributes[nameKey].toString()
    override val name: String
        get() = attributes["name"].toString()
    override val email: String
        get() {
            val email = attributes["email"]
            return email?.toString() ?: ""
        }
    override val imageUrl: String
        get() = attributes["avatar_url"].toString()
    override val firstName: String
        get() {
            val name = attributes["name"].toString()
            return name.split(" ")[0]
        }
    override val lastName: String
        get() {
            val name = attributes["name"].toString()
            val split = name.split(" ")
            return when (split.size) {
                1 -> ""
                2 -> split[1]
                else -> split.subList(1, split.size)
                    .joinToString(" ")
            }

        }

    override fun generateUsername(): String {
        val username = attributes["login"]
            ?.toString()
            ?: attributes["id"].toString()

        return username
    }
}