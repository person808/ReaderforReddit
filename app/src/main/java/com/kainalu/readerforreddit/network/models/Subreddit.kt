package com.kainalu.readerforreddit.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.threeten.bp.LocalDateTime

@JsonClass(generateAdapter = true)
data class Subreddit(
    override val created: LocalDateTime,
    @Json(name = "created_utc")
    override val createdUtc: LocalDateTime,
    override val id: String,
    override val name: String,
    @Json(name = "display_name")
    val displayName: String,
    @Json(name = "display_name_prefixed")
    val prefixedDisplayName: String,
    @Json(name = "icon_img")
    val iconUrl: String?
) : Thing, Created