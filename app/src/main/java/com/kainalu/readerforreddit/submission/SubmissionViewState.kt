package com.kainalu.readerforreddit.submission

import com.kainalu.readerforreddit.network.models.Link
import com.kainalu.readerforreddit.network.models.SubmissionItem

data class SubmissionViewState(
    val subreddit: String = "",
    val threadId: String = "",
    val sort: SubmissionSort? = null,
    val link: Link? = null,
    val comments: List<SubmissionItem> = emptyList()
)