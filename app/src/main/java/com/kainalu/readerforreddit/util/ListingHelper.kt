package com.kainalu.readerforreddit.util

import android.util.Log
import com.kainalu.readerforreddit.network.models.Listing
import retrofit2.HttpException
import retrofit2.Response

/**
 * Collects all elements in a [Listing] by making requests for new pages until there
 * are no more left.
 */
inline fun <T> collectListing(request: (String) -> Response<Listing<T>>): List<T> {
    var pageKey: String? = ""
    val result = mutableListOf<T>()
    while (pageKey != null) {
        val response = request.invoke(pageKey)
        if (response.isSuccessful) {
            response.body()?.children?.forEach {
                result.add(it)
            }
            pageKey = response.body()?.after
        } else {
            Log.e("CollectListing", "Error code: ${response.code()}")
            Log.e("CollectListing", response.errorBody().toString())
            throw HttpException(response)
        }
    }

    return result
}