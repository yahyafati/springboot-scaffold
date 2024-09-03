package com.yahyafati.springbootauthenticationscaffold.exceptions

import com.yahyafati.springbootauthenticationscaffold.exceptions.base.BaseException
import com.yahyafati.springbootauthenticationscaffold.exceptions.base.ExceptionType

class NotFoundException(
    message: String,
    override val exception: Exception? = null
) : BaseException(ExceptionType.NO_DATA_FOUND, message, exception) {

    override fun toString(): String {
        return "NotFoundException(message='$message', exception=$exception)"
    }
}