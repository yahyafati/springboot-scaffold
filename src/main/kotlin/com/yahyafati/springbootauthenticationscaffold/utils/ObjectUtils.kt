package com.yahyafati.springbootauthenticationscaffold.utils

object ObjectUtils {

    fun <T> convertObjectToClass(obj: Any, clazz: Class<T>): T {
        return clazz.cast(obj)
    }

    fun <T> convertListToClass(list: List<*>, clazz: Class<T>): List<T> {
        return list.map { clazz.cast(it) }
    }

    fun <K, V> convertMapToClass(map: Map<K, V>, keyClazz: Class<K>, valueClazz: Class<V>): Map<K, V> {
        return map.mapKeys { keyClazz.cast(it.key) }.mapValues { valueClazz.cast(it.value) }
    }
}