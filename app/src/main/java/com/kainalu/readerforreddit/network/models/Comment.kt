package com.kainalu.readerforreddit.network.models

import com.kainalu.readerforreddit.network.annotations.RedditModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.threeten.bp.LocalDateTime

@JsonClass(generateAdapter = true)
data class Comment(
    override val created: LocalDateTime,
    @Json(name = "created_utc")
    override val createdUtc: LocalDateTime,
    override val downs: Int,
    override val ups: Int,
    override val liked: Boolean?,
    val author: String,
    val body: String,
    val collapsed: Boolean,
    val depth: Int,
    val edited: EditInfo,
    val id: String,
    val name: String,
    val score: Int,
    @Json(name = "score_hidden")
    val scoreHidden: Boolean,
    val stickied: Boolean,
    @RedditModel
    val replies: Listing<SubmissionItem>
) : Votable, Created, SubmissionItem
