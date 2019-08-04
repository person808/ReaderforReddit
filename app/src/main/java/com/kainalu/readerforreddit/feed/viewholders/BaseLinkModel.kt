package com.kainalu.readerforreddit.feed.viewholders

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.card.MaterialCardView
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.models.LinkData
import com.kainalu.readerforreddit.models.getFormattedTitle
import com.kainalu.readerforreddit.util.KotlinEpoxyHolder
import com.kainalu.readerforreddit.util.getFormattedString
import com.kainalu.readerforreddit.util.getPostTime

abstract class BaseLinkModel<T : BaseHolder> : EpoxyModelWithHolder<T>() {

    @EpoxyAttribute
    lateinit var link: LinkData
    @EpoxyAttribute
    lateinit var onClick: View.OnClickListener

    override fun bind(holder: T) {
        super.bind(holder)
        with(link) {
            holder.container.setOnClickListener(onClick)
            with(holder.titleTextView) {
                text = link.getFormattedTitle(context)
            }
            with(holder.authorTextView) {
                text = context.getString(R.string.link_author_subtitle, author, createdUtc.getPostTime(context))
            }
            @SuppressLint("SetTextI18n")
            holder.subredditTextView.text = "r/$subreddit"
            with(numComments ?: 0) {
                val context = holder.commentTextView.context
                holder.commentTextView.text =
                    context.resources.getQuantityString(R.plurals.comments, this, getFormattedString())
            }
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