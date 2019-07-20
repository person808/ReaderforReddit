package com.kainalu.readerforreddit.feed.viewholders

import android.widget.TextView
import com.airbnb.epoxy.EpoxyModelClass
import com.kainalu.readerforreddit.R

@EpoxyModelClass(layout = R.layout.feed_link_web_item)
abstract class WebLinkModel : BaseLinkModel<WebLinkHolder>() {

    override fun bind(holder: WebLinkHolder) {
        super.bind(holder)
        holder.domainTextView.text = link.domain
    }
}

class WebLinkHolder : BaseHolder() {
    val domainTextView by bind<TextView>(R.id.domainTextView)
}
