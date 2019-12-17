package com.kainalu.readerforreddit.util

import android.util.Log
import com.kainalu.readerforreddit.network.models.Listing
import retrofit2.HttpException

/**
 * Collects all elements in a [Listing] by making requests for new pages until there
 * are no more left.
 */
inline fun <T> collectListing(request: (String) -> Listing<T>): List<T> {
    var pageKey: String? = ""
    val result = mutableListOf<T>()
    while (pageKey != null) {
        try {
            val listing = request.invoke(pageKey)
            listing.children.forEach {
                result.add(it)
            }
            pageKey = listing.after
        } catch (e: HttpException) {
            Log.e("CollectListing", e.message(), e)
            throw e
        }
    }

    return result
}