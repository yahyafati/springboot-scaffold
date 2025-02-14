package com.yahyafati.springbootauthenticationscaffold.exceptions

import com.yahyafati.springbootauthenticationscaffold.exceptions.base.BaseException
import com.yahyafati.springbootauthenticationscaffold.exceptions.base.ExceptionType

class BadRequestException(
    message: String,
    override val exception: Exception? = null
) : BaseException(ExceptionType.BAD_REQUEST, message, exception)