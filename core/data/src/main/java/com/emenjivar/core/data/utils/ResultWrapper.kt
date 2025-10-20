package com.emenjivar.core.data.utils

sealed class ResultWrapper<out T> {
    data class Success<out T>(val data: T) : ResultWrapper<T>()

    data class Error(val code: Int? = null, val message: String? = null)

    object Loading : ResultWrapper<Nothing>()
}
