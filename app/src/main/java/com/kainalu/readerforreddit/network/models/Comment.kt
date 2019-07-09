package com.kainalu.readerforreddit.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.threeten.bp.LocalDateTime

@JsonClass(generateAdapter = true)
data class Comment(
    @Enveloped
    val author: String,
    @Enveloped
    val id: String,
    @Enveloped
    val score: Int?,
    @Enveloped
    val body: String,
    @Enveloped
    val edited: EditInfo,
    @Enveloped
    @Json(name = "created_utc")
    val createdUtc: LocalDateTime,
    @Enveloped
    val replies: Listing<Comment>
)