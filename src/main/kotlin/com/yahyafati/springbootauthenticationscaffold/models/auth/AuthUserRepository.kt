package com.yahyafati.springbootauthenticationscaffold.models.auth

import com.yahyafati.springbootauthenticationscaffold.repo.base.IModelRepoSpecification
import org.springframework.data.jpa.repository.Query

interface AuthUserRepository : IModelRepoSpecification<AuthUser, Long> {

    @Query("select u from AuthUser u where upper(u.username) = upper(?1)")
    fun findByUsername(username: String): AuthUser?

}