package com.kainalu.readerforreddit.submission

import com.kainalu.readerforreddit.tree.AbstractSubmissionNode
import com.kainalu.readerforreddit.tree.AbstractSubmissionTree
import com.kainalu.readerforreddit.tree.LinkNode

data class SubmissionViewState(
    val subreddit: String = "",
    val threadId: String = "",
    val sort: SubmissionSort? = null,
    val submissionTree: AbstractSubmissionTree? = null,
    val link: LinkNode? = null,
    val comments: List<AbstractSubmissionNode<*>> = emptyList()
)