package com.burbon13.template.core


/**
 * A generic class that holds a value with its status.
 */
sealed class MyResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : MyResult<T>()
    data class Error(val message: String) : MyResult<Nothing>()

    val succeeded
        get() = this is Success

    val failed
        get() = this is Error

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$message]"
        }
    }
}
