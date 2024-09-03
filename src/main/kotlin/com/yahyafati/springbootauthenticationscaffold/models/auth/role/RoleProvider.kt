package com.yahyafati.springbootauthenticationscaffold.models.auth.role

import com.yahyafati.springbootauthenticationscaffold.exceptions.InternalServerErrorException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RoleProvider(
    private val roleRepository: RoleRepository
) {

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(RoleProvider::class.java)
    }

    private val roles: List<Role> = roleRepository.findAll()
    private val DEFAULT_ROLE = "ROLE_USER"

    init {
        LOG.info("RoleProvider initialized")
    }

    fun getDefaultRole(): Role {
        return roles
            .find { it.name == DEFAULT_ROLE }
            ?: throw InternalServerErrorException("Default role '$DEFAULT_ROLE' not found")
    }

    final fun getRoleByName(name: String): Role? {
        return roles.find { it.name == name }
    }


}