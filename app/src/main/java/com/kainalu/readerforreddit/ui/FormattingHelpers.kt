package com.kainalu.readerforreddit.ui

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.TextAppearanceSpan
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.util.getFormattedString

object FormattingHelpers {

    /**
     * Formats a submission title and score into a pretty [Spanned] string.
     *
     * @param context the context to use
     * @param title the title of the submission
     * @param score the score of the submission
     */
    fun formatSubmissionTitle(context: Context, title: String, score: Int): Spanned {
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
}