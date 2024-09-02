package com.yahyafati.springbootauthenticationscaffold.config.security.authenticationfacade

import com.yahyafati.springbootauthenticationscaffold.models.auth.User
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component


@Component
class AuthenticationFacade : IAuthenticationFacade {

    class NoUserInSessionException : UsernameNotFoundException("No user in session")


    override val authentication: Authentication?
        get() = SecurityContextHolder.getContext().authentication

    override val currentUser: User?
        get() {
            val principal = authentication?.principal
            if (principal is User) {
                return principal
            }
            if (principal == null) {
                return null
            }
            throw IllegalStateException("Principal is not a User")
        }

    override val currentUsername: String?
        get() = currentUser?.username
    override val currentUserId: Long?
        get() = currentUser?.id

    override val forcedCurrentUser: User
        get() = currentUser ?: throw NoUserInSessionException()
    override val forcedCurrentUsername: String
        get() = forcedCurrentUser.username
    override val forcedCurrentUserId: Long
        get() = forcedCurrentUser.id


}