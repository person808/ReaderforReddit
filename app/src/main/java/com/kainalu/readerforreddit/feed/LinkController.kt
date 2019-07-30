package com.kainalu.readerforreddit.feed

import android.view.View
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.kainalu.readerforreddit.feed.viewholders.ImagePostModel_
import com.kainalu.readerforreddit.feed.viewholders.SelfPostModel_
import com.kainalu.readerforreddit.feed.viewholders.WebLinkModel_
import com.kainalu.readerforreddit.network.models.Link
import com.kainalu.readerforreddit.ui.sortHeader

class LinkController(
    private val headerClickListener: View.OnClickListener,
    private val linkClickListener: LinkClickListener
) : PagedListEpoxyController<Link>() {

    interface LinkClickListener {
        fun onLinkClicked(link: Link)
    }

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
            "image" -> ImagePostModel_()
                .link(item)
                .onClick { _ -> linkClickListener.onLinkClicked(item) }
                .id(item.id)
            "link" -> WebLinkModel_()
                .link(item)
                .onClick { _ -> linkClickListener.onLinkClicked(item) }
                .id(item.id)
            "self" -> SelfPostModel_()
                .link(item)
                .onClick { _ -> linkClickListener.onLinkClicked(item) }
                .id(item.id)
            else -> SelfPostModel_()
                .link(item)
                .onClick { _ -> linkClickListener.onLinkClicked(item) }
                .id(item.id)
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