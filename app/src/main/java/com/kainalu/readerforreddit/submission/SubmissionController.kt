package com.kainalu.readerforreddit.submission

import android.view.View
import com.airbnb.epoxy.Typed3EpoxyController
import com.kainalu.readerforreddit.models.LinkData
import com.kainalu.readerforreddit.submission.viewholders.*
import com.kainalu.readerforreddit.tree.*
import com.kainalu.readerforreddit.ui.sortHeader

class SubmissionController(
    private val commentClickListener: CommentClickListener,
    private val sortClickListener: View.OnClickListener,
    private val linkClickListener: LinkClickListener,
    private val moreClickListener: MoreClickListener
) : Typed3EpoxyController<LinkNode, List<AbstractNode>, SubmissionSort>() {

    interface CommentClickListener {
        fun onCommentClicked(commentNode: CommentNode)
    }

    interface LinkClickListener {
        fun onLinkClicked(link: LinkData)
    }

    interface MoreClickListener {
        fun onMoreClicked(more: MoreNode)
    }

    override fun buildModels(linkNode: LinkNode?, comments: List<AbstractNode>?, sort: SubmissionSort?) {
        linkNode?.let {
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
                is CommentNode -> if (it.visibility == VisibilityState.VISIBLE || it.visibility == VisibilityState.COLLAPSED) {
                    comment {
                        comment(it)
                        onClick { _ -> commentClickListener.onCommentClicked(it) }
                        id(it.id)
                    }
                }
                is MoreNode -> if (it.visibility == VisibilityState.VISIBLE) {
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