package com.kainalu.readerforreddit.network.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Listing<T>(
    @field:Enveloped val children: List<T>,
    val before: String?,
    val after: String?
)

