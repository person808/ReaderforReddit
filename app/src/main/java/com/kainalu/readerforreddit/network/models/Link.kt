package com.kainalu.readerforreddit.network.models

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.TextAppearanceSpan
import androidx.recyclerview.widget.DiffUtil
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.util.getFormattedString
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.threeten.bp.LocalDateTime

@JsonClass(generateAdapter = true)
data class Link(
    override val created: LocalDateTime,
    @Json(name = "created_utc")
    override val createdUtc: LocalDateTime,
    override val id: String,
    override val name: String,
    override val downs: Int,
    override val ups: Int,
    override val liked: Boolean?,
    val author: String,
    val domain: String?,
    val edited: EditInfo,
    @Json(name = "is_self")
    val isSelfPost: Boolean?,
    @Json(name = "num_comments")
    val numComments: Int?,
    @Json(name = "post_hint")
    val postHint: String?,
    val preview: PreviewInfo?,
    val score: Int,
    val selftext: String,
    val subreddit: String,
    val thumbnail: String,
    val title: String,
    val url: String
) : Votable, Created, SubmissionItem {
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

fun Link.getFormattedTitle(context: Context): Spanned {
    val span = SpannableString("${score.getFormattedString()} $title")
    val firstSpaceIndex = span.indexOfFirst { it == ' ' }
    span.setSpan(
        TextAppearanceSpan(context, R.style.LinkScoreTextAppearance),
        0,
        firstSpaceIndex,
        Spannable.SPAN_INCLUSIVE_EXCLUSIVE
    )
    return span
}