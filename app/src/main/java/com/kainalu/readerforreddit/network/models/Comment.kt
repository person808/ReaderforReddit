package com.kainalu.readerforreddit.network.models

import com.kainalu.readerforreddit.network.annotations.RedditModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.threeten.bp.LocalDateTime

@JsonClass(generateAdapter = true)
data class Comment(
    val author: String,
    val id: String,
    val name: String,
    val score: Int,
    @Json(name = "score_hidden")
    val scoreHidden: Boolean,
    val stickied: Boolean,
    override val ups: Int,
    override val downs: Int,
    override val liked: Boolean?,
    override val created: LocalDateTime,
    @Json(name = "created_utc")
    override val createdUtc: LocalDateTime,
    val body: String,
    val edited: EditInfo,
    @RedditModel
    val replies: Listing<SubmissionItem>,
    val depth: Int,
    val collapsed: Boolean
) : Votable, Created, SubmissionItem
