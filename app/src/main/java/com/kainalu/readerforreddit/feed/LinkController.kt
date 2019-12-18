package com.kainalu.readerforreddit.feed

import android.view.View
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.kainalu.readerforreddit.feed.viewholders.ImagePostModel_
import com.kainalu.readerforreddit.feed.viewholders.SelfPostModel_
import com.kainalu.readerforreddit.feed.viewholders.WebLinkModel_
import com.kainalu.readerforreddit.models.LinkData
import com.kainalu.readerforreddit.ui.sortHeader

class LinkController(
    private val headerClickListener: View.OnClickListener,
    private val linkClickListener: LinkClickListener
) : PagedListEpoxyController<LinkData>() {

    interface LinkClickListener {
        fun onLinkClicked(link: LinkData)
    }

    var headerLabel = ""
        set(value) {
            if (value != field) {
                field = value
                requestModelBuild()
            }
        }

    override fun buildItemModel(currentPosition: Int, item: LinkData?): EpoxyModel<*> {
        if (item == null) {
            throw NullPointerException()
        }

        return when (item.postHint) {
            "image" -> ImagePostModel_()
                .author(item.author)
                .createdTime(item.createdUtc)
                .numComments(item.numComments ?: 0)
                .score(item.score)
                .subreddit(item.subreddit)
                .title(item.title)
                .preview(item.preview)
                .url(item.url)
                .onClick { _ -> linkClickListener.onLinkClicked(item) }
                .id(item.id)
            "link" -> WebLinkModel_()
                .author(item.author)
                .createdTime(item.createdUtc)
                .numComments(item.numComments ?: 0)
                .score(item.score)
                .subreddit(item.subreddit)
                .title(item.title)
                .onClick { _ -> linkClickListener.onLinkClicked(item) }
                .id(item.id)
            "self" -> SelfPostModel_()
                .author(item.author)
                .createdTime(item.createdUtc)
                .numComments(item.numComments ?: 0)
                .score(item.score)
                .subreddit(item.subreddit)
                .title(item.title)
                .selfText(item.selftext)
                .onClick { _ -> linkClickListener.onLinkClicked(item) }
                .id(item.id)
            else -> SelfPostModel_()
                .author(item.author)
                .createdTime(item.createdUtc)
                .numComments(item.numComments ?: 0)
                .score(item.score)
                .subreddit(item.subreddit)
                .title(item.title)
                .selfText(item.selftext)
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