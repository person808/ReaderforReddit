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
    val score: Int,
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
    span.setSpan(TextAppearanceSpan(context, R.style.LinkScoreTextAppearance), 0, firstSpaceIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    return span
}