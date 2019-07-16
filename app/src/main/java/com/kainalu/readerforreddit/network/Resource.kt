package com.kainalu.readerforreddit.network

sealed class Resource<T>(open val data: T? = null) {
    class Success<T>(override val data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(data: T? = null, val error: Throwable?) : Resource<T>(data)
}
