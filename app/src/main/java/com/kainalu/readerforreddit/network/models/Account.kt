package com.kainalu.readerforreddit.network.models

import com.squareup.moshi.Json
import org.threeten.bp.LocalDateTime

data class Account(
    override val created: LocalDateTime,
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