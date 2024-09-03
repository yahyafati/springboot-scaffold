package com.yahyafati.springbootauthenticationscaffold.models.auth.role._dto

import com.yahyafati.springbootauthenticationscaffold.models.auth.role.Role
import jakarta.validation.constraints.NotBlank

data class CreateUpdateRoleDTO(
    @field:NotBlank(message = "Role name is required")
    val name: String
) {
    fun toRole(): Role {
        return Role(
            name = name
        )
    }
}
