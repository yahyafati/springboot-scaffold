package com.yahyafati.springbootauthenticationscaffold.utils

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import kotlin.reflect.KClass

object ServletUtils {

    private val mapper: ObjectMapper = ObjectMapper()
    private val currentRequest
        get() = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request


    fun <T : Any> getParameter(name: String, type: KClass<T>): T? {
        val value: String = currentRequest.getParameter(name) ?: return null
        return mapper.readValue(value, type.java)
    }

    fun <T : Any> getListParameter(name: String, type: KClass<T>): List<T>? {
        val value = currentRequest.getParameterValues(name) ?: return null
        val list = value.toList()
        return ObjectUtils.convertListToClass(list, type.java)
    }
}