package com.kainalu.readerforreddit.subreddit

import com.kainalu.readerforreddit.feed.Duration
import com.kainalu.readerforreddit.feed.SubredditSort

data class SubredditViewState(
    val subreddit: String,
    val sort: SubredditSort,
    val sortDuration: Duration = Duration.NONE,
    val availableSorts: List<SubredditSort>,
    val loading: Boolean = false
)
