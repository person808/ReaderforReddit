package com.kainalu.readerforreddit.submission

import android.view.View
import com.airbnb.epoxy.Typed3EpoxyController
import com.kainalu.readerforreddit.network.models.Link
import com.kainalu.readerforreddit.network.models.More
import com.kainalu.readerforreddit.submission.viewholders.*
import com.kainalu.readerforreddit.tree.AbstractSubmissionNode
import com.kainalu.readerforreddit.tree.CommentNode
import com.kainalu.readerforreddit.tree.LinkNode
import com.kainalu.readerforreddit.tree.MoreNode
import com.kainalu.readerforreddit.ui.sortHeader

class SubmissionController(
    private val commentClickListener: CommentClickListener,
    private val sortClickListener: View.OnClickListener,
    private val linkClickListener: LinkClickListener,
    private val moreClickListener: MoreClickListener
) : Typed3EpoxyController<LinkNode, List<AbstractSubmissionNode<*>>, SubmissionSort>() {

    interface CommentClickListener {
        fun onCommentClicked(commentNode: CommentNode)
    }

    interface LinkClickListener {
        fun onLinkClicked(link: Link)
    }

    interface MoreClickListener {
        fun onMoreClicked(more: More)
    }

    override fun buildModels(linkNode: LinkNode?, comments: List<AbstractSubmissionNode<*>>?, sort: SubmissionSort?) {
        linkNode?.data?.let {
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
        if (sort != null && linkNode != null) {
            sortHeader {
                labelRes(sort.label)
                onClick(sortClickListener)
                id("sort")
            }
        }

        comments?.forEach {
            when (it) {
                is CommentNode -> if (!it.data.hidden) {
                    comment {
                        comment(it)
                        onClick { _ -> commentClickListener.onCommentClicked(it) }
                        id(it.data.id)
                    }
                }
                is MoreNode -> if (!it.data.hidden) {
                    more {
                        data(it.data)
                        onClick { _ -> moreClickListener.onMoreClicked(it.data) }
                        id(it.data.id)
                    }
                }
            }
        }
    }
}