package com.yahyafati.springbootauthenticationscaffold.models.auth.role

import com.yahyafati.springbootauthenticationscaffold.config.security.authenticationfacade.IAuthenticationFacade
import com.yahyafati.springbootauthenticationscaffold.services.IModelService
import org.springframework.stereotype.Service
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

@Service
class RoleService(
    override val repository: RoleRepository,
    override val authenticationFacade: IAuthenticationFacade
) : IModelService<Role> {
    override val modelProperties: Collection<KProperty1<Role, *>>
        get() = Role::class.memberProperties

}