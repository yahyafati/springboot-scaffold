package com.yahyafati.springbootauthenticationscaffold.exceptions

import com.yahyafati.springbootauthenticationscaffold.exceptions.base.BaseException
import com.yahyafati.springbootauthenticationscaffold.exceptions.base.ExceptionType

class UnprocessableEntityException(
    message: String,
    override val exception: Exception? = null
) : BaseException(ExceptionType.UNPROCESSABLE_ENTITY, message, exception) {

    override fun toString(): String {
        return "UnprocessableEntityException(message='$message', exception=$exception)"
    }
}