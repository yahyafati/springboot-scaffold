package com.yahyafati.springbootauthenticationscaffold.models.auth

import com.yahyafati.springbootauthenticationscaffold.config.security.authenticationfacade.IAuthenticationFacade
import com.yahyafati.springbootauthenticationscaffold.exceptions.BadRequestException
import com.yahyafati.springbootauthenticationscaffold.exceptions.NoEntityFoundException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

@Service
class UserServices(
    override val repository: UserRepository,
    override val authenticationFacade: IAuthenticationFacade
) : IUserServices {
    override val modelProperties: Collection<KProperty1<User, *>>
        get() = User::class.memberProperties

    override fun findByUsername(username: String): User? {
        return repository.findByUsername(username)
    }

    override fun getLoggedInUser(): User {
        TODO("Not yet implemented")
    }

    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) {
            throw BadRequestException("Username cannot be null")
        }
        return findByUsername(username) ?: throw NoEntityFoundException.create(User::class.java, "username", username)
    }
}