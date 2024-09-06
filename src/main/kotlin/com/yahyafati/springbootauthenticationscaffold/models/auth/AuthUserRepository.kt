package com.yahyafati.springbootauthenticationscaffold.models.auth

import com.yahyafati.springbootauthenticationscaffold.repo.base.IModelRepoSpecification
import org.springframework.data.jpa.repository.Query

interface AuthUserRepository : IModelRepoSpecification<AuthUser> {

    @Query("select u from AuthUser u where upper(u.username) = upper(?1)")
    fun findByUsername(username: String): AuthUser?

    @Query("select u from AuthUser u where upper(u.email) = upper(?1)")
    fun findByEmail(email: String): AuthUser?

    @Query("select u from AuthUser u where upper(u.username) = upper(?1)")
    fun existsByUsername(uniqueUsername: String): Boolean

}