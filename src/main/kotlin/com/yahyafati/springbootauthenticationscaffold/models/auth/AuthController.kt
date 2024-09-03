package com.yahyafati.springbootauthenticationscaffold.models.auth

import com.yahyafati.springbootauthenticationscaffold.models.auth._dto.AuthUserDto
import com.yahyafati.springbootauthenticationscaffold.models.auth._dto.RegisterUserDto
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
        registerUser: RegisterUserDto
    ): AuthUserDto {
        return AuthUserDto(userService.registerUser(registerUser))
    }

}