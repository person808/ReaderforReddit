package com.kainalu.readerforreddit.feed

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.network.models.Link
import com.kainalu.readerforreddit.util.getPostTime

class LinkPagedAdapter : PagedListAdapter<Link, LinkPagedAdapter.BaseViewHolder>(Link.DIFF_CALLBACK) {

    open class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleTextView = view.findViewById<TextView>(R.id.titleTextView)
        private val authorTextView = view.findViewById<TextView>(R.id.authorTextView)
        private val subredditTextView = view.findViewById<TextView>(R.id.subredditTextView)
        protected val context = view.context

        open fun bindTo(link: Link) {
            titleTextView.text = link.title
            authorTextView.text =
                context.getString(R.string.link_author_subtitle, link.author, link.createdUtc.getPostTime(context))
            @SuppressLint("SetTextI18n")
            subredditTextView.text = "r/${link.subreddit}"
        }
    }

    class ImageViewHolder(view: View) : BaseViewHolder(view) {
        private val imageView = view.findViewById<ImageView>(R.id.linkImageView)

        override fun bindTo(link: Link) {
            super.bindTo(link)
            if (link.postHint == "image") {
                imageView.visibility = View.VISIBLE
                Glide.with(imageView).clear(imageView)
                Glide.with(imageView)
                    .load(link.url)
                    .apply(RequestOptions().override(Target.SIZE_ORIGINAL))
                    .into(imageView)
            } else {
                imageView.visibility = View.GONE
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)?.postHint) {
            "image" -> R.layout.feed_link_image_item
            else -> R.layout.feed_link_image_item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.feed_link_image_item -> ImageViewHolder(itemView)
            else -> BaseViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        getItem(position)?.let { holder.bindTo(it) }
    }
}