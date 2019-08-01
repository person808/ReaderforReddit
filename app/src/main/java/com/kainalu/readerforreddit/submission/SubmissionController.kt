package com.kainalu.readerforreddit.submission

import android.view.View
import com.airbnb.epoxy.Typed3EpoxyController
import com.kainalu.readerforreddit.network.models.Comment
import com.kainalu.readerforreddit.network.models.Link
import com.kainalu.readerforreddit.network.models.More
import com.kainalu.readerforreddit.network.models.SubmissionItem
import com.kainalu.readerforreddit.submission.viewholders.*
import com.kainalu.readerforreddit.ui.sortHeader

class SubmissionController(
    private val commentClickListener: CommentClickListener,
    private val sortClickListener: View.OnClickListener,
    private val linkClickListener: LinkClickListener,
    private val moreClickListener: MoreClickListener
) : Typed3EpoxyController<Link, List<SubmissionItem>, SubmissionSort>() {

    interface CommentClickListener {
        fun onCommentClicked(comment: Comment)
    }

    interface LinkClickListener {
        fun onLinkClicked(link: Link)
    }

    interface MoreClickListener {
        fun onMoreClicked(more: More)
    }

    override fun buildModels(link: Link?, comments: List<SubmissionItem>?, sort: SubmissionSort?) {
        link?.let {
            when (it.postHint) {
                "image" -> imageSubmission {
                    link(it)
                    id(it.id)
                }
                "link" -> webSubmission {
                    link(it)
                    onLinkClick { _ -> linkClickListener.onLinkClicked(it) }
                    id(it.id)
                }
                "self" -> selfSubmission {
                    link(it)
                    id(it.id)
                }
                else -> selfSubmission {
                    link(it)
                    id(it.id)
                }
            }
        }

        // Ensure we don't show the sort header if the link is not loaded yet
        if (sort != null && link != null) {
            sortHeader {
                labelRes(sort.label)
                onClick(sortClickListener)
                id("sort")
            }
        }

        comments?.forEach {
            when (it) {
                is Comment -> if (!it.hidden) {
                    comment {
                        comment(it)
                        onClick { _ -> commentClickListener.onCommentClicked(it) }
                        id(it.id)
                    }
                }
                is More -> if (!it.hidden) {
                    more {
                        data(it)
                        onClick { _ -> moreClickListener.onMoreClicked(it) }
                        id(it.id)
                    }
                }
            }
        }
    }
}