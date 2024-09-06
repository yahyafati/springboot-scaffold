package com.yahyafati.springbootauthenticationscaffold.models.auth.provider

import com.yahyafati.springbootauthenticationscaffold.config.security.authenticationfacade.IAuthenticationFacade
import com.yahyafati.springbootauthenticationscaffold.exceptions.InternalServerErrorException
import com.yahyafati.springbootauthenticationscaffold.models.auth.AuthUser
import com.yahyafati.springbootauthenticationscaffold.services.IModelService
import org.springframework.stereotype.Service
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

@Service
class OAuth2UserProviderService(
    override val repository: OAuth2UserProviderRepository,
    override val authenticationFacade: IAuthenticationFacade
) : IModelService<OAuth2UserProvider> {
    override val modelProperties: Collection<KProperty1<OAuth2UserProvider, *>>
        get() = OAuth2UserProvider::class.memberProperties

    fun findByUserId(userId: Long): List<OAuth2UserProvider> {
        return repository.findByUser(AuthUser(id = userId))
    }


    fun findByProviderTypeAndProviderId(providerType: EOAuth2Provider, providerId: String): OAuth2UserProvider? {
        return repository.findByProviderTypeAndProviderId(providerType, providerId)
    }

    fun updateByUserAndProvider(
        user: AuthUser,
        providerType: EOAuth2Provider,
        provider: OAuth2UserProvider
    ): OAuth2UserProvider {
        val existingProvider = repository
            .findByUserAndProviderType(user, providerType)
            ?: throw InternalServerErrorException("Provider not found")

        if (existingProvider.user != user) {
            throw InternalServerErrorException("User mismatch")
        }
        return existingProvider.apply {
            this.providerType = provider.providerType
            this.user = provider.user
        }
    }
}