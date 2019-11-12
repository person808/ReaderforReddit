package com.kainalu.readerforreddit.submission

import com.kainalu.readerforreddit.tree.AbstractNode
import com.kainalu.readerforreddit.tree.LinkNode

data class SubmissionViewState(
    val subreddit: String = "",
    val threadId: String = "",
    val sort: SubmissionSort? = null,
    val submissionTree: LinkNode? = null,
    val comments: List<AbstractNode>? = null,
    val loading: Boolean = false
)