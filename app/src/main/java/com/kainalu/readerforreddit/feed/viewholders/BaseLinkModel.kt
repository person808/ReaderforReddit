package com.kainalu.readerforreddit.feed.viewholders

import android.annotation.SuppressLint
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.network.models.Link
import com.kainalu.readerforreddit.util.KotlinEpoxyHolder
import com.kainalu.readerforreddit.util.getFormattedString
import com.kainalu.readerforreddit.util.getPostTime

abstract class BaseLinkModel<T: BaseHolder> : EpoxyModelWithHolder<T>() {

    @EpoxyAttribute lateinit var link: Link

    override fun bind(holder: T) {
        super.bind(holder)
        with(link) {
            holder.titleTextView.text = title
            with(holder.authorTextView) {
                text = context.getString(R.string.link_author_subtitle, author, createdUtc.getPostTime(context))
            }
            with(holder.scoreTextView) {
                text = score?.getFormattedString() ?: context.getString(R.string.vote)
            }
            @SuppressLint("SetTextI18n")
            holder.subredditTextView.text = "r/$subreddit"
            holder.commentTextView.text = numComments?.getFormattedString() ?: ""
        }
    }
}

abstract class BaseHolder : KotlinEpoxyHolder() {
    val titleTextView by bind<TextView>(R.id.titleTextView)
    val authorTextView by bind<TextView>(R.id.authorTextView)
    val subredditTextView by bind<TextView>(R.id.subredditTextView)
    val scoreTextView by bind<TextView>(R.id.scoreTextView)
    val commentTextView by bind<TextView>(R.id.commentTextView)
}