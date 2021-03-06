package com.github.rmitsubayashi.domain

import com.github.rmitsubayashi.domain.error.GeneralError
import com.github.rmitsubayashi.domain.error.IError

data class Resource<out T>(val data: T?, val error: IError?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(data, null)
        }
        fun <T> success(): Resource<T> {
            return Resource(null, null)
        }
        fun <T> error(error: IError): Resource<T> {
            return Resource(null, error)
        }
        fun <T> error(): Resource<T> {
            return Resource(null, GeneralError.ERROR)
        }
    }
}