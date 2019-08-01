package com.kainalu.readerforreddit.network.models

import com.kainalu.readerforreddit.network.annotations.RedditModel
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class Listing<T>(
    @RedditModel
    val children: List<T>,
    val before: String? = null,
    val after: String? = null
) {
    companion object {
        fun empty(): Listing<Any> = Listing(children = emptyList())
    }
}


