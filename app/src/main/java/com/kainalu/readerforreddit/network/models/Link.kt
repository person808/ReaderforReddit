package com.kainalu.readerforreddit.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.threeten.bp.LocalDateTime

@JsonClass(generateAdapter = true)
data class Link(
    val subreddit: String,
    val score: Int?,
    @Json(name = "num_comments")
    val numComments: Int?,
    val id: String,
    @Json(name = "created_utc")
    val createdUtc: LocalDateTime,
    val thumbnail: String,
    val edited: EditInfo,
    val author: String,
    val title: String,
    @Json(name = "is_self")
    val isSelfPost: Boolean?,
    val selftext: String,
    val url: String
)