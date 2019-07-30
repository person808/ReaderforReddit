package com.kainalu.readerforreddit.submission

import com.kainalu.readerforreddit.network.models.SubmissionItem

data class SubmissionViewState(
    val subreddit: String = "",
    val threadId: String = "",
    val sort: SubmissionSort = SubmissionSort.BEST,
    val submission: List<SubmissionItem> = emptyList()
)