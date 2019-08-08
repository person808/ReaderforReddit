package com.kainalu.readerforreddit.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.threeten.bp.LocalDateTime

@JsonClass(generateAdapter = true)
data class Account(
    override val created: LocalDateTime,
    @Json(name = "created_utc")
    override val createdUtc: LocalDateTime,
    override val id: String,
    @Json(name = "comment_karma")
    val commentKarma: Int,
    @Json(name = "link_karma")
    val linkKarma: Int,
    @Json(name = "name")
    val username: String
) : Thing, Created {
    override val name: String = "t2_$id"
}