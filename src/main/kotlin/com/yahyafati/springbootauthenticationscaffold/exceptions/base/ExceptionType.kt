package com.yahyafati.springbootauthenticationscaffold.exceptions.base

import org.springframework.http.HttpStatus

enum class ExceptionType(val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST) {

    NO_DATA_FOUND(HttpStatus.NOT_FOUND),
    BAD_REQUEST(HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED),
    FORBIDDEN(HttpStatus.FORBIDDEN),
    CONFLICT(HttpStatus.CONFLICT),
    UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST),
    DATA_INTEGRITY_ERROR(HttpStatus.CONFLICT),
}