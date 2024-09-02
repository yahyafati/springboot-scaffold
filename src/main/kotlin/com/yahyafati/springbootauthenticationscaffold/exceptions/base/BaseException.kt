package com.yahyafati.springbootauthenticationscaffold.exceptions.base

open class BaseException(
    val type: ExceptionType,
    message: String,
    exception: Exception? = null
) : RuntimeException(message, exception)