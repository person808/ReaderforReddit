package com.kainalu.readerforreddit.feed

import com.kainalu.readerforreddit.network.ApiService
import com.kainalu.readerforreddit.network.models.Link
import com.kainalu.readerforreddit.network.models.Listing
import javax.inject.Inject

class FeedRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getFeed(subreddit: String = "", sort: SubredditSort, after: String = ""): Listing<Link> {
        if (subreddit.isNotEmpty() && sort == SubredditSort.BEST) {
            throw IllegalArgumentException("Subreddit sort ${sort.name} is invalid for $subreddit")
        }

        return if (subreddit.isNotEmpty()) {
            apiService.getSubreddit(subreddit, sort.name.toLowerCase(), after)
        } else {
            apiService.getSubreddit(sort.name.toLowerCase(), after)
        }
    }
}