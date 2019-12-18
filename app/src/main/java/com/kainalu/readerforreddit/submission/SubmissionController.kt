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
                    author(it.author)
                    createdTime(it.createdUtc)
                    numComments(it.numComments ?: 0)
                    score(it.score)
                    subreddit(it.subreddit)
                    title(it.title)

                    preview(it.preview)
                    url(it.url)
                    id(it.id)
                }
                "link" -> webSubmission {
                    author(it.author)
                    createdTime(it.createdUtc)
                    numComments(it.numComments ?: 0)
                    score(it.score)
                    subreddit(it.subreddit)
                    title(it.title)

                    domain(it.domain ?: "")
                    preview(it.preview)
                    onLinkClick { _ -> linkClickListener.onLinkClicked(it) }
                    id(it.id)
                }
                "self" -> selfSubmission {
                    author(it.author)
                    createdTime(it.createdUtc)
                    numComments(it.numComments ?: 0)
                    score(it.score)
                    subreddit(it.subreddit)
                    title(it.title)

                    selfText(it.selftext)
                    id(it.id)
                }
                else -> selfSubmission {
                    author(it.author)
                    createdTime(it.createdUtc)
                    numComments(it.numComments ?: 0)
                    score(it.score)
                    subreddit(it.subreddit)
                    title(it.title)

                    selfText(it.selftext)
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
                        author(it.author)
                        body(it.body)
                        createdTime(it.createdUtc)
                        depth(it.depth)
                        numChildren(it.countChildren())
                        score(it.score)
                        scoreHidden(it.scoreHidden)
                        visibility(it.visibility)
                        onClick { _ -> commentClickListener.onCommentClicked(it) }
                        id(it.id)
                    }
                }
                is MoreNode -> if (it.visibility == VisibilityState.VISIBLE && it.childIds.isNotEmpty()) {
                    more {
                        depth(it.depth)
                        numChildren(it.childIds.size)
                        isLoading(it.loading)
                        onClick { _ -> moreClickListener.onMoreClicked(it) }
                        id(it.id)
                    }
                }
            }
        }
    }

}