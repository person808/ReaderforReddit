package com.kainalu.readerforreddit.feed.viewholders

import com.airbnb.epoxy.EpoxyModelClass
import com.kainalu.readerforreddit.R

@EpoxyModelClass(layout = R.layout.feed_link_web_item)
abstract class WebLinkModel : BaseLinkModel<WebLinkHolder>()

class WebLinkHolder : BaseHolder()
