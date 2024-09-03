package com.yahyafati.springbootauthenticationscaffold.models.auth

import com.yahyafati.springbootauthenticationscaffold.config.security.authenticationfacade.IAuthenticationFacade
import com.yahyafati.springbootauthenticationscaffold.exceptions.BadRequestException
import com.yahyafati.springbootauthenticationscaffold.exceptions.NoEntityFoundException
import com.yahyafati.springbootauthenticationscaffold.models.auth._dto.RegisterUserDTO
import com.yahyafati.springbootauthenticationscaffold.models.auth.role.RoleProvider
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

@Service
class AuthServices(
    override val repository: AuthUserRepository,
    override val authenticationFacade: IAuthenticationFacade,
    val passwordEncoder: PasswordEncoder,
    val roleProvider: RoleProvider
) : IAuthServices {
    override val modelProperties: Collection<KProperty1<AuthUser, *>>
        get() = AuthUser::class.memberProperties

    override fun findByUsername(username: String): AuthUser? {
        return repository.findByUsername(username)
    }

    override fun getLoggedInUser(): AuthUser {
        TODO("Not yet implemented")
    }

    override fun registerUser(registerUser: RegisterUserDTO): AuthUser {
        val user = registerUser.toAuthUser()
        user.role = roleProvider.getDefaultRole()
        user.password = passwordEncoder.encode(user.password)
        return saveNew(user)
    }

    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) {
            throw BadRequestException("Username cannot be null")
        }
        return findByUsername(username) ?: throw NoEntityFoundException.create(
            AuthUser::class.java,
            "username",
            username
        )
    }
}