package com.kainalu.readerforreddit.feed.viewholders

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.kainalu.readerforreddit.R

@EpoxyModelClass(layout = R.layout.feed_link_self_item)
abstract class SelfPostModel : BaseLinkModel<SelfPostHolder>() {

    @EpoxyAttribute
    lateinit var selfText: String
    override fun bind(holder: SelfPostHolder) {
        super.bind(holder)
        with(holder.selfTextView) {
            visibility = if (selfText.isEmpty()) View.GONE else View.VISIBLE
            text = selfText
        }
    }
}

class SelfPostHolder : BaseHolder() {
    val selfTextView by bind<TextView>(R.id.selfTextView)
}