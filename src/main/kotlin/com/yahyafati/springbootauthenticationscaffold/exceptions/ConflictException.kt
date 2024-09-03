package com.yahyafati.springbootauthenticationscaffold.exceptions

import com.yahyafati.springbootauthenticationscaffold.exceptions.base.BaseException
import com.yahyafati.springbootauthenticationscaffold.exceptions.base.ExceptionType

class ConflictException(
    message: String,
    override val exception: Exception? = null
) : BaseException(ExceptionType.CONFLICT, message, exception) {

    override fun toString(): String {
        return "ConflictException(message='$message', exception=$exception)"
    }
}