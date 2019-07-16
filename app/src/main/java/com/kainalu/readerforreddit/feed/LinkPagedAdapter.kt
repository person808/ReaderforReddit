package com.kainalu.readerforreddit.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.network.models.Link

class LinkPagedAdapter : PagedListAdapter<Link, LinkPagedAdapter.ViewHolder>(Link.DIFF_CALLBACK) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleTextView = view.findViewById<TextView>(R.id.titleTextView)

        fun bindTo(link: Link) {
            titleTextView.text = link.title
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