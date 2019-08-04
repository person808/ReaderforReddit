package com.kainalu.readerforreddit.network.models

import com.kainalu.readerforreddit.network.annotations.RedditModel
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class Listing<T>(
    val after: String? = null,
    val before: String? = null,
    @RedditModel
    val children: List<T>
) {
    companion object {
        fun empty(): Listing<Any> = Listing(children = emptyList())
    }
}


