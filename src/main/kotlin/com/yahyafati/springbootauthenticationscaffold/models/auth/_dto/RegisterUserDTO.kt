package com.yahyafati.springbootauthenticationscaffold.models.auth._dto

import com.yahyafati.springbootauthenticationscaffold.models.auth.AuthUser
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegisterUserDTO(
    @field:NotBlank(message = "Username is required")
    val username: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, message = "Password must be at least 6 characters long")
    val password: String,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email is invalid")
    val email: String
) {

    fun toAuthUser() = AuthUser(
        username = username,
        password = password,
        email = email
    )
}