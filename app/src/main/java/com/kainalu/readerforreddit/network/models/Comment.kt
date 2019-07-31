package com.kainalu.readerforreddit.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.threeten.bp.LocalDateTime

@JsonClass(generateAdapter = true)
data class Comment(
    val author: String,
    val id: String,
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
    val replies: Listing<HideableSubmissionItem>,
    val depth: Int,
    var collapsed: Boolean,
    @Transient
    override var hidden: Boolean = false
) : Votable, Created, HideableSubmissionItem

fun Comment.countChildren(): Int {
    return if (replies.children.isEmpty()) {
        0
    } else {
        var total = 0
        replies.children.forEach {
            if (it is Comment) {
                total += it.countChildren()
            }
        }
        total += replies.children.size
        total
    }
}