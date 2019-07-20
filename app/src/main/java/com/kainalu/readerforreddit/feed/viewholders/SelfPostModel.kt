package com.kainalu.readerforreddit.feed.viewholders

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyModelClass
import com.kainalu.readerforreddit.R

@EpoxyModelClass(layout = R.layout.feed_link_self_item)
abstract class SelfPostModel : BaseLinkModel<SelfPostHolder>() {

    override fun bind(holder: SelfPostHolder) {
        super.bind(holder)
        with(holder.selfTextView) {
            visibility = if (link.selftext.isEmpty()) View.GONE else View.VISIBLE
            text = link.selftext
        }
    }
}

class SelfPostHolder : BaseHolder() {
    val selfTextView by bind<TextView>(R.id.selfTextView)
}