package com.yahyafati.springbootauthenticationscaffold.exceptions

import com.yahyafati.springbootauthenticationscaffold.exceptions.base.BaseException
import com.yahyafati.springbootauthenticationscaffold.exceptions.base.ExceptionType

class ForbiddenException(
    message: String,
    override val exception: Exception? = null
) : BaseException(ExceptionType.FORBIDDEN, message, exception) {

    override fun toString(): String {
        return "ForbiddenException(message='$message', exception=$exception)"
    }
}