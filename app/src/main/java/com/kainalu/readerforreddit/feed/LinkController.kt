package com.kainalu.readerforreddit.feed

import android.view.View
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.kainalu.readerforreddit.feed.viewholders.ImagePostModel_
import com.kainalu.readerforreddit.feed.viewholders.SelfPostModel_
import com.kainalu.readerforreddit.feed.viewholders.WebLinkModel_
import com.kainalu.readerforreddit.feed.viewholders.sortHeader
import com.kainalu.readerforreddit.network.models.Link

class LinkController(
    private val headerClickListener: View.OnClickListener
) : PagedListEpoxyController<Link>() {

    var headerLabel = ""
        set(value) {
            if (value != field) {
                field = value
                requestModelBuild()
            }
        }

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

    override fun addModels(models: List<EpoxyModel<*>>) {
        sortHeader {
            label(headerLabel)
            onClick(headerClickListener)
            id("header")
        }
        super.add(models)
    }
}