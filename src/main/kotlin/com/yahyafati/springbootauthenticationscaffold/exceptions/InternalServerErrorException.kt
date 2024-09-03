package com.yahyafati.springbootauthenticationscaffold.exceptions

import com.yahyafati.springbootauthenticationscaffold.exceptions.base.BaseException
import com.yahyafati.springbootauthenticationscaffold.exceptions.base.ExceptionType

class InternalServerErrorException(
    message: String,
    override val exception: Exception? = null
) : BaseException(ExceptionType.INTERNAL_SERVER_ERROR, message, exception) {

    override fun toString(): String {
        return "InternalServerErrorException(message='$message', exception=$exception)"
    }
}