package com.kainalu.readerforreddit.models

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.TextAppearanceSpan
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.network.models.PreviewInfo
import com.kainalu.readerforreddit.util.getFormattedString
import org.threeten.bp.LocalDateTime

/**
 * Interface to represent fields we extract from the Reddit API for link data.
 *
 * @see [com.kainalu.readerforreddit.network.models.Link]
 */
interface LinkData {
    val author: String
    val createdUtc: LocalDateTime
    val domain: String?
    val id: String
    val name: String
    val numComments: Int?
    val postHint: String?
    val preview: PreviewInfo?
    val score: Int
    val selftext: String
    val subreddit: String
    val title: String
    val url: String
}

fun LinkData.getFormattedTitle(context: Context): Spanned {
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
