package com.yahyafati.springbootauthenticationscaffold.exceptions

import com.yahyafati.springbootauthenticationscaffold.exceptions.base.BaseException
import com.yahyafati.springbootauthenticationscaffold.exceptions.base.ExceptionType

class ServiceUnavailableException(
    message: String,
    override val exception: Exception? = null
) : BaseException(ExceptionType.SERVICE_UNAVAILABLE, message, exception) {

    override fun toString(): String {
        return "ServiceUnavailable(message='$message', exception=$exception)"
    }
}