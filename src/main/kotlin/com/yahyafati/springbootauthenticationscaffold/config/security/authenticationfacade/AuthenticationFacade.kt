package com.yahyafati.springbootauthenticationscaffold.config.security.authenticationfacade

import com.yahyafati.springbootauthenticationscaffold.models.auth.AuthUser
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component


@Component
class AuthenticationFacade : IAuthenticationFacade {

    class NoUserInSessionException : UsernameNotFoundException("No user in session")


    override val authentication: Authentication?
        get() = SecurityContextHolder.getContext().authentication

    override val currentAuthUser: AuthUser?
        get() {
            val principal = authentication?.principal
            if (principal is AuthUser) {
                return principal
            }
            if (principal == null) {
                return null
            }
            throw IllegalStateException("Principal is not a User")
        }

    override val currentUsername: String?
        get() = currentAuthUser?.username
    override val currentUserId: Long?
        get() = currentAuthUser?.id

    override val forcedCurrentAuthUser: AuthUser
        get() = currentAuthUser ?: throw NoUserInSessionException()
    override val forcedCurrentUsername: String
        get() = forcedCurrentAuthUser.username
    override val forcedCurrentUserId: Long
        get() = forcedCurrentAuthUser.id


}