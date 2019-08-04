package com.kainalu.readerforreddit.submission

import com.kainalu.readerforreddit.tree.AbstractNode
import com.kainalu.readerforreddit.tree.LinkNode
import com.kainalu.readerforreddit.tree.SubmissionTree

data class SubmissionViewState(
    val subreddit: String = "",
    val threadId: String = "",
    val sort: SubmissionSort? = null,
    val submissionTree: SubmissionTree? = null,
    val link: LinkNode? = null,
    val comments: List<AbstractNode>? = null
)