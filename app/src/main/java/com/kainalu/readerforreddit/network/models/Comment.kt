package com.kainalu.readerforreddit.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.threeten.bp.LocalDateTime

@JsonClass(generateAdapter = true)
data class Comment(
    val author: String,
    val id: String,
    val score: Int?,
    val body: String,
    val edited: EditInfo,
    @Json(name = "created_utc")
    val createdUtc: LocalDateTime,
    @field:Enveloped
    val replies: Listing<Comment>
)