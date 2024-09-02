package com.yahyafati.springbootauthenticationscaffold.exceptions

import com.yahyafati.springbootauthenticationscaffold.exceptions.base.BaseException
import com.yahyafati.springbootauthenticationscaffold.exceptions.base.ExceptionType

class NoEntityFoundException(
    message: String,
    private val exception: Exception? = null
) : BaseException(ExceptionType.NO_DATA_FOUND, message, exception) {

    companion object {

        fun create(entityName: String, idName: String, id: Any): NoEntityFoundException {
            return NoEntityFoundException("No $entityName found with $idName: $id")
        }

        fun create(entityName: String, id: Any): NoEntityFoundException {
            return create(entityName, "id", id)
        }

        fun create(entity: Class<*>, id: Any): NoEntityFoundException {
            return create(entity, "id", id)
        }

        fun create(entity: Class<*>, idName: String, id: Any): NoEntityFoundException {
            return create(entity.simpleName, idName, id)
        }
    }

    override fun toString(): String {
        return "NoEntityFoundException(message='$message', exception=$exception)"
    }
}