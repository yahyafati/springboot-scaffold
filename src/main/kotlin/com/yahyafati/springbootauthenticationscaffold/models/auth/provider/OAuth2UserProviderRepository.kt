package com.yahyafati.springbootauthenticationscaffold.models.auth.provider

import com.yahyafati.springbootauthenticationscaffold.models.auth.AuthUser
import com.yahyafati.springbootauthenticationscaffold.repo.base.IModelRepoSpecification
import org.springframework.data.jpa.repository.Query

interface OAuth2UserProviderRepository : IModelRepoSpecification<OAuth2UserProvider> {

    fun findByProviderTypeAndProviderId(providerType: EOAuth2Provider, providerId: String): OAuth2UserProvider?

    fun findByUserAndProviderType(user: AuthUser, providerType: EOAuth2Provider): OAuth2UserProvider?

    @Query("SELECT p.user FROM OAuth2UserProvider p WHERE p.providerType = ?1 AND p.providerId = ?2")
    fun findUserByProvider(providerType: EOAuth2Provider, providerId: String): AuthUser?

}