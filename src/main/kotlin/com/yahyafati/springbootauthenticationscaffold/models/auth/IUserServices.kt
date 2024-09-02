package com.yahyafati.springbootauthenticationscaffold.models.auth

import com.yahyafati.springbootauthenticationscaffold.services.IModelService
import org.springframework.security.core.userdetails.UserDetailsService

interface IUserServices : UserDetailsService, IModelService<User> {

    fun findByUsername(username: String): User?
    fun getLoggedInUser(): User

}