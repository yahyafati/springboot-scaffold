package com.yahyafati.springbootauthenticationscaffold.exceptions.base

class BaseErrorModel(
    val type: ExceptionType,
    val message: String,
    val status: Int,
) {
}