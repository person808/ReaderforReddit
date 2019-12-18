package com.kainalu.readerforreddit.feed.viewholders

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.card.MaterialCardView
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.ui.FormattingHelpers
import com.kainalu.readerforreddit.util.KotlinEpoxyHolder
import com.kainalu.readerforreddit.util.getFormattedString
import com.kainalu.readerforreddit.util.getPostTime
import org.threeten.bp.LocalDateTime

abstract class BaseLinkModel<T : BaseHolder> : EpoxyModelWithHolder<T>() {

    @EpoxyAttribute
    lateinit var author: String
    @EpoxyAttribute
    lateinit var createdTime: LocalDateTime
    @EpoxyAttribute
    var numComments: Int = 0
    @EpoxyAttribute
    var score: Int = 0
    @EpoxyAttribute
    lateinit var subreddit: String
    @EpoxyAttribute
    lateinit var title: String
    @EpoxyAttribute
    lateinit var onClick: View.OnClickListener

    override fun bind(holder: T) {
        super.bind(holder)
        holder.container.setOnClickListener(onClick)
        with(holder.titleTextView) {
            text = FormattingHelpers.formatSubmissionTitle(context, title, score)
        }
        with(holder.authorTextView) {
            text = context.getString(
                R.string.link_author_subtitle,
                author,
                createdTime.getPostTime(context)
            )
        }
        @SuppressLint("SetTextI18n")
        holder.subredditTextView.text = "r/$subreddit"
        with(numComments) {
            val context = holder.commentTextView.context
            holder.commentTextView.text =
                context.resources.getQuantityString(R.plurals.comments, this, getFormattedString())
        }
    }
}

abstract class BaseHolder : KotlinEpoxyHolder() {
    val container by bind<MaterialCardView>(R.id.linkContainer)
    val titleTextView by bind<TextView>(R.id.titleTextView)
    val authorTextView by bind<TextView>(R.id.authorTextView)
    val subredditTextView by bind<TextView>(R.id.subredditTextView)
    val commentTextView by bind<TextView>(R.id.commentTextView)
}