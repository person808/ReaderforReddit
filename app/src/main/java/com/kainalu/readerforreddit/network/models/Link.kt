package com.kainalu.readerforreddit.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.threeten.bp.LocalDateTime

@JsonClass(generateAdapter = true)
data class Link(
    @Enveloped
    val subreddit: String,
    @Enveloped
    val score: Int?,
    @Enveloped
    @Json(name = "num_comments")
    val numComments: Int?,
    @Enveloped
    val id: String,
    @Enveloped
    @Json(name = "created_utc")
    val createdUtc: LocalDateTime,
    @Enveloped
    val thumbnail: String,
    @Enveloped
    val edited: EditInfo,
    @Enveloped
    val author: String,
    @Enveloped
    val title: String,
    @Enveloped
    @Json(name = "is_self")
    val isSelfPost: Boolean?,
    @Enveloped
    val selftext: String,
    @Enveloped
    val url: String
)