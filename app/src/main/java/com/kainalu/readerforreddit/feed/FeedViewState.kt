package com.kainalu.readerforreddit.feed

data class FeedViewState(
    val subreddit: String,
    val sort: SubredditSort,
    val sortDuration: Duration = Duration.NONE,
    val availableSorts: List<SubredditSort>
)