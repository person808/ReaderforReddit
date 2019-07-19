package com.kainalu.readerforreddit.feed

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.kainalu.readerforreddit.GlideApp
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.network.models.Link
import com.kainalu.readerforreddit.util.getFormattedString
import com.kainalu.readerforreddit.util.getPostTime
import com.kainalu.readerforreddit.util.setDrawableTint

class LinkPagedAdapter : PagedListAdapter<Link, LinkPagedAdapter.BaseViewHolder>(Link.DIFF_CALLBACK) {

    open class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        protected val titleTextView = view.findViewById<TextView>(R.id.titleTextView)
        protected val authorTextView = view.findViewById<TextView>(R.id.authorTextView)
        protected val subredditTextView = view.findViewById<TextView>(R.id.subredditTextView)
        protected val scoreTextView = view.findViewById<TextView>(R.id.scoreTextView)
        protected val commentTextView = view.findViewById<TextView>(R.id.commentTextView)
        protected val upvoteButton = view.findViewById<ImageButton>(R.id.upvoteButton)
        protected val downvoteButton = view.findViewById<ImageButton>(R.id.downvoteButton)
        protected val context = view.context

        open fun bindTo(link: Link) {
            with(link) {
                titleTextView.text = title
                authorTextView.text =
                    context.getString(R.string.link_author_subtitle, author, createdUtc.getPostTime(context))
                @SuppressLint("SetTextI18n")
                subredditTextView.text = "r/${subreddit}"
                scoreTextView.text = if (score != null) score.getFormattedString() else context.getString(R.string.vote)
                commentTextView.text = if (numComments != null) numComments.getFormattedString() else ""
            }
        }
    }

    class ImageViewHolder(view: View) : BaseViewHolder(view) {
        private val imageView = view.findViewById<ImageView>(R.id.linkImageView)

        override fun bindTo(link: Link) {
            super.bindTo(link)
            imageView.visibility = if (link.preview == null) View.GONE else View.VISIBLE
            link.preview?.let { previewInfo ->
                // Make sure we measure the view's dimensions after layout by using view.post()
                // Otherwise we might receive a width of 0
                imageView.post {
                    // Resize the imageview so that it can fit the whole image while
                    // maintaining aspect ration
                    val imageWidthRatio = imageView.width.toDouble() / previewInfo.width
                    val imageViewHeightPx = previewInfo.height * imageWidthRatio
                    imageView.updateLayoutParams { height = imageViewHeightPx.toInt() }
                }
                GlideApp.with(imageView)
                    .load(link.url)
                    .apply(RequestOptions().override(Target.SIZE_ORIGINAL))
                    .into(imageView)
                // We set the tint here so that gifs are also tinted
                imageView.setColorFilter(ContextCompat.getColor(context, R.color.imageOverlay), PorterDuff.Mode.SRC_ATOP)
                commentTextView.setDrawableTint(commentTextView.currentTextColor)
            }
        }
    }

    class SelfTextViewHolder(view: View) : BaseViewHolder(view) {
        private val selfTextView = view.findViewById<TextView>(R.id.selfTextView)

        override fun bindTo(link: Link) {
            super.bindTo(link)
            selfTextView.apply {
                visibility = if (link.selftext.isEmpty()) View.GONE else View.VISIBLE
                selfTextView.text = link.selftext
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)?.postHint) {
            "image" -> R.layout.feed_link_image_item
            "self" -> R.layout.feed_link_self_item
            else -> R.layout.feed_link_self_item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.feed_link_image_item -> ImageViewHolder(itemView)
            R.layout.feed_link_self_item -> SelfTextViewHolder(itemView)
            else -> SelfTextViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        getItem(position)?.let { holder.bindTo(it) }
    }
}