package com.yahyafati.springbootauthenticationscaffold.exceptions

import com.yahyafati.springbootauthenticationscaffold.exceptions.base.BaseException
import com.yahyafati.springbootauthenticationscaffold.exceptions.base.ExceptionType

class UnauthorizedException(
    message: String,
    override val exception: Exception? = null
) : BaseException(ExceptionType.UNAUTHORIZED, message, exception) {

    override fun toString(): String {
        return "UnauthorizedException(message='$message', exception=$exception)"
    }
}