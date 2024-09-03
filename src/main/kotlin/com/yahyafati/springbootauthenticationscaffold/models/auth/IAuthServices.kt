package com.yahyafati.springbootauthenticationscaffold.models.auth

import com.yahyafati.springbootauthenticationscaffold.models.auth._dto.RegisterUserDTO
import com.yahyafati.springbootauthenticationscaffold.services.IModelService
import org.springframework.security.core.userdetails.UserDetailsService

interface IAuthServices : UserDetailsService, IModelService<AuthUser> {

    fun findByUsername(username: String): AuthUser?
    fun getLoggedInUser(): AuthUser
    fun registerUser(registerUser: RegisterUserDTO): AuthUser

}