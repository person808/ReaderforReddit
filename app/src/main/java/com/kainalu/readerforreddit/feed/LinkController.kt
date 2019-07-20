package com.kainalu.readerforreddit.feed

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.kainalu.readerforreddit.feed.viewholders.ImagePostModel_
import com.kainalu.readerforreddit.feed.viewholders.SelfPostModel_
import com.kainalu.readerforreddit.feed.viewholders.WebLinkModel_
import com.kainalu.readerforreddit.network.models.Link

class LinkController : PagedListEpoxyController<Link>() {

    override fun buildItemModel(currentPosition: Int, item: Link?): EpoxyModel<*> {
        if (item == null) {
            throw NullPointerException()
        }

        return when (item.postHint) {
            "image" -> ImagePostModel_().link(item).id(item.id)
            "link" -> WebLinkModel_().link(item).id(item.id)
            "self" -> SelfPostModel_().link(item).id(item.id)
            else -> SelfPostModel_().link(item).id(item.id)
        }
    }
}