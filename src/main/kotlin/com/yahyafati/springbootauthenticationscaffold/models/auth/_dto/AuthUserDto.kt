package com.yahyafati.springbootauthenticationscaffold.models.auth._dto

import com.yahyafati.springbootauthenticationscaffold.models.auth.AuthUser
import java.io.Serializable

/**
 * DTO for {@link com.yahyafati.springbootauthenticationscaffold.models.auth.AuthUser}
 */
data class AuthUserDto(
    val id: Long = 0L,
    val username: String = "",
    val email: String = "",
    val enabled: Boolean = false,
    val roleName: String = ""
) : Serializable {

    constructor(authUser: AuthUser) : this(
        id = authUser.id,
        username = authUser.username,
        email = authUser.email,
        enabled = authUser.isEnabled,
        roleName = authUser.role.name
    )
}