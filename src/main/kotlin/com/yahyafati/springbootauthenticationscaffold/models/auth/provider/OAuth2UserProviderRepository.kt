package com.yahyafati.springbootauthenticationscaffold.models.auth.provider

import com.yahyafati.springbootauthenticationscaffold.models.auth.AuthUser
import com.yahyafati.springbootauthenticationscaffold.repo.base.IModelRepoSpecification

interface OAuth2UserProviderRepository : IModelRepoSpecification<OAuth2UserProvider> {

    fun findByProviderTypeAndProviderId(providerType: EOAuth2Provider, providerId: String): OAuth2UserProvider?

    fun findByUserAndProviderType(user: AuthUser, providerType: EOAuth2Provider): OAuth2UserProvider?

    fun findByUser(user: AuthUser): List<OAuth2UserProvider>

}