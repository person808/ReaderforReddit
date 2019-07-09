package com.kainalu.readerforreddit.network.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Listing<T>(
    @Enveloped val children: List<T>,
    @Enveloped val before: String?,
    @Enveloped val after: String?
)

