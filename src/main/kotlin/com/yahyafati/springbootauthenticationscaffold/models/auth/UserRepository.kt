package com.yahyafati.springbootauthenticationscaffold.models.auth

import com.yahyafati.springbootauthenticationscaffold.repo.base.IModelRepoSpecification
import org.springframework.data.jpa.repository.Query

interface UserRepository : IModelRepoSpecification<User, Long> {

    @Query("select u from User u where upper(u.username) = upper(?1)")
    fun findByUsername(username: String): User?

}