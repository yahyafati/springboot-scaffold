package com.yahyafati.springbootauthenticationscaffold.models.auth.role._dto

import com.yahyafati.springbootauthenticationscaffold.models.auth.role.Role


/**
 * DTO for {@link com.yahyafati.springbootauthenticationscaffold.models.auth.role.Role}
 */
data class RoleDTO(val id: Long = 0L, val name: String = "") {
    constructor(role: Role) : this(role.id, role.name)
}