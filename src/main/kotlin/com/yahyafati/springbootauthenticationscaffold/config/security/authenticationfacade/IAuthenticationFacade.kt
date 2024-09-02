package com.yahyafati.springbootauthenticationscaffold.config.security.authenticationfacade

import com.yahyafati.springbootauthenticationscaffold.models.auth.User
import org.springframework.security.core.Authentication

interface IAuthenticationFacade {

    companion object {

        fun equals(username1: String?, username2: String?): Boolean {
            if (username1 == null || username2 == null) return false
            return username1.equals(username2, ignoreCase = true)
        }

    }

    val authentication: Authentication?

    val currentUser: User?
    val currentUsername: String?
    val currentUserId: Long?

    val forcedCurrentUser: User
    val forcedCurrentUsername: String
    val forcedCurrentUserId: Long


}