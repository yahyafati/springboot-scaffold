package com.yahyafati.springbootauthenticationscaffold.models.auth.provider

enum class EOAuth2Provider(val registrationId: String) {

    GOOGLE("google"),
    FACEBOOK("facebook"),
    GITHUB("github");

    companion object {
        fun from(registrationId: String?): EOAuth2Provider {
            return entries.firstOrNull { it.registrationId == registrationId }
                ?: throw IllegalArgumentException("Unknown OAuth2 provider: $registrationId")
        }
    }
}