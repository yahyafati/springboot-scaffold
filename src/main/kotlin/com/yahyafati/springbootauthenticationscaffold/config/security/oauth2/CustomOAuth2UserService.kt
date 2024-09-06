package com.yahyafati.springbootauthenticationscaffold.config.security.oauth2

import com.fasterxml.jackson.databind.ObjectMapper
import com.yahyafati.springbootauthenticationscaffold.config.security.oauth2.userinfo.OAuth2UserInfo
import com.yahyafati.springbootauthenticationscaffold.models.auth.AuthUser
import com.yahyafati.springbootauthenticationscaffold.models.auth.IAuthServices
import com.yahyafati.springbootauthenticationscaffold.models.auth.provider.EOAuth2Provider
import com.yahyafati.springbootauthenticationscaffold.models.auth.provider.OAuth2UserProvider
import com.yahyafati.springbootauthenticationscaffold.models.auth.provider.OAuth2UserProviderService
import com.yahyafati.springbootauthenticationscaffold.models.auth.role.RoleProvider
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.random.Random

@Service
class CustomOAuth2UserService(
    private val userServices: IAuthServices,
    private val roleProvider: RoleProvider,
    private val oAuth2UserProviderService: OAuth2UserProviderService,
    private val mapper: ObjectMapper
) : DefaultOAuth2UserService() {

    companion object {
        private val LOG = LoggerFactory.getLogger(CustomOAuth2UserService::class.java)
    }

    init {
        LOG.info("CustomOAuth2UserService initialized")
    }

    @Transactional
    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {
        LOG.info("Loading user from OAuth2 provider")
        val oAuth2User = super.loadUser(userRequest)
        return try {
            processOAuth2User(userRequest!!, oAuth2User)
        } catch (ex: AuthenticationException) {
            throw ex
        } catch (ex: Exception) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw InternalAuthenticationServiceException(ex.message, ex.cause)
        }
    }

    @Transactional
    fun processOAuth2User(oAuth2UserRequest: OAuth2UserRequest, oAuth2User: OAuth2User): OAuth2User {
        val oAuth2UserInfo: OAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
            oAuth2UserRequest.clientRegistration.registrationId,
            oAuth2User.attributes
        )

        val provider = EOAuth2Provider.from(oAuth2UserRequest.clientRegistration.registrationId)

        var user: AuthUser? = if (!oAuth2UserInfo.email.isNullOrEmpty()) {
            userServices.findByEmail(oAuth2UserInfo.email!!)
        } else {
            null
        }
        val oAuth2UserProvider = oAuth2UserProviderService.findByProviderTypeAndProviderId(
            provider,
            oAuth2UserInfo.id
        )
        var isNewUser = false

        if (user == null) {
            user = oAuth2UserProviderService.findUserByProvider(provider, oAuth2UserInfo.id)
        }

        if (user == null) {
            isNewUser = true
            LOG.info("User with email ${oAuth2UserInfo.email} does not exist. Registering new user...")
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo)
        }

        if (oAuth2UserProvider == null) {
            val providerEntity = OAuth2UserProvider(
                providerId = oAuth2UserInfo.id,
                providerType = provider,
                user = user,
                providerData = mapper.writeValueAsString(oAuth2UserInfo)
            )
            oAuth2UserProviderService.save(providerEntity)
        }

        updateExistingUser(user, oAuth2UserInfo, provider)

        return oAuth2UserInfo
            .toCustomOAuth2User(user).apply { newUser = isNewUser }
    }

    private fun generateUniqueUsernameFromEmail(username: String): String {
        val prefix = Random.nextInt(100, 999).toString()
        val uniqueUsername = "${username}_${prefix}"
//        while (userServices.existsByUsername(uniqueUsername)) {
//            prefix += Random.nextInt(100, 999).toString()
//            uniqueUsername = "${username}_${prefix}"
//        }
        return uniqueUsername
    }

    @Transactional
    fun registerNewUser(oAuth2UserRequest: OAuth2UserRequest, oAuth2UserInfo: OAuth2UserInfo): AuthUser {
        val user = AuthUser()

        user.email = oAuth2UserInfo.email
        user.username = generateUniqueUsernameFromEmail(oAuth2UserInfo.generateUsername())
        user.isEnabled = true
        user.role = roleProvider.getDefaultRole()
        return userServices.saveNew(user)
    }

    @Transactional
    fun updateExistingUser(
        existingUser: AuthUser,
        oAuth2UserInfo: OAuth2UserInfo,
        provider: EOAuth2Provider
    ): OAuth2UserProvider {
        existingUser.isEnabled = true
        return oAuth2UserProviderService.updateByUserAndProvider(
            existingUser,
            provider,
            OAuth2UserProvider(
                providerType = provider,
                user = existingUser,
                providerData = mapper.writeValueAsString(oAuth2UserInfo)
            )
        )
    }
}