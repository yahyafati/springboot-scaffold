package com.yahyafati.springbootauthenticationscaffold.exceptions.controllers

import com.yahyafati.springbootauthenticationscaffold.exceptions.NotFoundException
import com.yahyafati.springbootauthenticationscaffold.exceptions.base.BaseErrorModel
import com.yahyafati.springbootauthenticationscaffold.exceptions.base.ExceptionType
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@RestControllerAdvice
class ClientExceptionHandler : ResponseEntityExceptionHandler() {

    companion object {
        private val LOG = LoggerFactory.getLogger(ClientExceptionHandler::class.java)
    }

    init {
        LOG.info("ClientExceptionHandler initialized")
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleResourceNotFoundException(ex: NotFoundException): ResponseEntity<BaseErrorModel> {
        val error = BaseErrorModel.fromBaseException(ex)
        return ResponseEntity(error, ex.type.httpStatus)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(ex: DataIntegrityViolationException): ResponseEntity<BaseErrorModel> {
        val error = BaseErrorModel(
            type = ExceptionType.DATA_INTEGRITY_ERROR,
            message = "Data integrity error",
            extra = ex.mostSpecificCause.message
        )
        return ResponseEntity(error, error.type.httpStatus)
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val errors = ex.bindingResult.fieldErrors
            .groupBy { it.field }
            .map { (field, errors) ->
                field to errors.map { it.defaultMessage }
            }.toMap()

        val error = BaseErrorModel(
            type = ExceptionType.VALIDATION_ERROR,
            status = HttpStatus.BAD_REQUEST.value(),
            message = "Validation error",
            extra = errors
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }
}