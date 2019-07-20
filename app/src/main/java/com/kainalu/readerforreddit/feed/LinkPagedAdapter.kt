package com.kainalu.readerforreddit.feed

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.paging.PagedListAdapter
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
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
        private val infoLayout = view.findViewById<ViewGroup>(R.id.infoLayout)
        private val requestListener = object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                resource?.toBitmap()?.let { bitmap ->
                    Palette.Builder(bitmap).maximumColorCount(24).generate { palette ->
                        palette?.mutedSwatch?.let { swatch ->
                            infoLayout.setBackgroundColor(swatch.rgb)
                            titleTextView.setTextColor(swatch.titleTextColor)
                            subredditTextView.setTextColor(swatch.titleTextColor)
                            authorTextView.setTextColor(swatch.bodyTextColor)
                            scoreTextView.setTextColor(swatch.bodyTextColor)
                            commentTextView.setTextColor(swatch.bodyTextColor)
                            commentTextView.setDrawableTint(swatch.bodyTextColor)
                            upvoteButton.setColorFilter(swatch.bodyTextColor, PorterDuff.Mode.SRC_ATOP)
                            downvoteButton.setColorFilter(swatch.bodyTextColor, PorterDuff.Mode.SRC_ATOP)
                        }
                    }
                }
                return false
            }
        }

        override fun bindTo(link: Link) {
            super.bindTo(link)
            imageView.visibility = if (link.preview == null) View.GONE else View.VISIBLE
            link.preview?.let { previewInfo ->
                GlideApp.with(imageView)
                    .load(link.url)
                    .apply(RequestOptions().override(Target.SIZE_ORIGINAL))
                    .listener(requestListener)
                    .into(imageView)
            }
        }
    }

    class WebLinkViewHolder(view: View) : BaseViewHolder(view) {
        private val domainTextView = view.findViewById<TextView>(R.id.domainTextView)

        override fun bindTo(link: Link) {
            super.bindTo(link)
            domainTextView.text = link.domain
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
            "link" -> R.layout.feed_link_web_item
            "self" -> R.layout.feed_link_self_item
            else -> R.layout.feed_link_self_item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.feed_link_image_item -> ImageViewHolder(itemView)
            R.layout.feed_link_web_item -> WebLinkViewHolder(itemView)
            R.layout.feed_link_self_item -> SelfTextViewHolder(itemView)
            else -> SelfTextViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        getItem(position)?.let { holder.bindTo(it) }
    }
}