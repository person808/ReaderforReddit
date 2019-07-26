package com.kainalu.readerforreddit.network.models

import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.threeten.bp.LocalDateTime

@JsonClass(generateAdapter = true)
data class Link(
    val subreddit: String,
    @Json(name = "num_comments")
    val numComments: Int?,
    val id: String,
    override val created: LocalDateTime,
    @Json(name = "created_utc")
    override val createdUtc: LocalDateTime,
    override val ups: Int,
    override val downs: Int,
    override val liked: Boolean?,
    val score: Int?,
    val thumbnail: String,
    val edited: EditInfo,
    val author: String,
    @Json(name = "post_hint")
    val postHint: String?,
    val preview: PreviewInfo?,
    val title: String,
    @Json(name = "is_self")
    val isSelfPost: Boolean?,
    val selftext: String,
    val domain: String?,
    val url: String
) : Votable, Created {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Link>() {
            override fun areItemsTheSame(oldItem: Link, newItem: Link): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Link, newItem: Link): Boolean {
                return oldItem == newItem
            }
        }
    }
}