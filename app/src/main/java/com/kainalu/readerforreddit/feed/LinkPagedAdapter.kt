package com.kainalu.readerforreddit.feed

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.network.models.Link
import com.kainalu.readerforreddit.util.getPostTime

class LinkPagedAdapter : PagedListAdapter<Link, LinkPagedAdapter.ViewHolder>(Link.DIFF_CALLBACK) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleTextView = view.findViewById<TextView>(R.id.titleTextView)
        private val authorTextView = view.findViewById<TextView>(R.id.authorTextView)
        private val subredditTextView = view.findViewById<TextView>(R.id.subredditTextView)
        private val context = view.context

        fun bindTo(link: Link) {
            titleTextView.text = link.title
            authorTextView.text =
                context.getString(R.string.link_author_subtitle, link.author, link.createdUtc.getPostTime(context))
            @SuppressLint("SetTextI18n")
            subredditTextView.text = "r/${link.subreddit}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_link_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bindTo(it) }
    }
}