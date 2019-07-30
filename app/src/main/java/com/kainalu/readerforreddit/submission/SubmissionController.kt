package com.kainalu.readerforreddit.submission

import android.view.View
import com.airbnb.epoxy.Typed2EpoxyController
import com.kainalu.readerforreddit.network.models.Comment
import com.kainalu.readerforreddit.network.models.Link
import com.kainalu.readerforreddit.network.models.More
import com.kainalu.readerforreddit.network.models.SubmissionItem
import com.kainalu.readerforreddit.submission.viewholders.*
import com.kainalu.readerforreddit.ui.sortHeader

class SubmissionController(
    private val commentClickListener: CommentClickListener,
    private val sortClickListener: View.OnClickListener
) : Typed2EpoxyController<List<SubmissionItem>, SubmissionSort>() {

    interface CommentClickListener {
        fun onCommentClicked(comment: Comment)
    }

    override fun buildModels(items: List<SubmissionItem>?, sort: SubmissionSort?) {
        if (sort == null) {
            throw NullPointerException("subreddit_sort should not be null")
        }

        items?.forEach {
            when (it) {
                is Link -> {
                    when (it.postHint) {
                        "image" -> imageSubmission {
                            link(it)
                            id(it.id)
                        }
                        "link" -> webSubmission {
                            link(it)
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
                    sortHeader {
                        labelRes(sort.label)
                        onClick(sortClickListener)
                        id("subreddit_sort")
                    }
                }
                is Comment -> if (!it.collapsed && !it.hidden) {
                    comment {
                        comment(it)
                        onClick { _ -> commentClickListener.onCommentClicked(it) }
                        id(it.id)
                    }
                } else if (!it.hidden) {
                    collapsedComment {
                        comment(it)
                        onClick { _ -> commentClickListener.onCommentClicked(it) }
                        id(it.id)
                    }
                }
                is More -> if (!it.hidden) {
                    more {
                        data(it)
                        onClick { _ -> }
                        id(it.id)
                    }
                }
            }
        }
    }
}