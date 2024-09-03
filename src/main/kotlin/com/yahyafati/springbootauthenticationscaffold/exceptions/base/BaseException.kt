package com.yahyafati.springbootauthenticationscaffold.exceptions.base

open class BaseException(
    val type: ExceptionType,
    override val message: String,
    open val exception: Exception? = null
) : RuntimeException(message, exception)