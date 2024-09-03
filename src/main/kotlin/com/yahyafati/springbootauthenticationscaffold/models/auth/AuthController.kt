package com.yahyafati.springbootauthenticationscaffold.models.auth

import com.yahyafati.springbootauthenticationscaffold.models.auth._dto.AuthUserDTO
import com.yahyafati.springbootauthenticationscaffold.models.auth._dto.RegisterUserDTO
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "User Authentication API")
class AuthController(
    private val userService: AuthServices
) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun registerUser(
        @RequestBody
        @Validated
        registerUser: RegisterUserDTO
    ): AuthUserDTO {
        return AuthUserDTO(userService.registerUser(registerUser))
    }

}