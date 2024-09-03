package com.yahyafati.springbootauthenticationscaffold.exceptions.base

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class BaseErrorModel(
    val type: ExceptionType,
    val message: String,
    val status: Int = type.httpStatus.value(),
    val extra: Any? = null
) {

    companion object {

        fun fromBaseException(exception: BaseException): BaseErrorModel {
            return BaseErrorModel(
                type = exception.type,
                message = exception.message,
                status = exception.type.httpStatus.value()
            )
        }
    }
}