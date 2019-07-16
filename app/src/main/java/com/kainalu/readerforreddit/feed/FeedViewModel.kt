package com.kainalu.readerforreddit.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.kainalu.readerforreddit.network.models.Link
import javax.inject.Inject

class FeedViewModel @Inject constructor(private val feedRepository: FeedRepository) : ViewModel() {

    private lateinit var feed: LiveData<PagedList<Link>>

    fun getFeed(subreddit: String = ""): LiveData<PagedList<Link>> {
        if (!::feed.isInitialized) {
            feed = feedRepository.getFeed("", SubredditSort.BEST)
        }
        return feed
    }
}