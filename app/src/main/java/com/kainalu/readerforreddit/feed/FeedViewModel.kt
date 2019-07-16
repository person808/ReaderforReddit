package com.kainalu.readerforreddit.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.kainalu.readerforreddit.network.models.Link
import javax.inject.Inject

class FeedViewModel @Inject constructor(private val feedRepository: FeedRepository) : ViewModel() {

    private lateinit var feed: LiveData<PagedList<Link>>
    private val pagingConfig = Config(pageSize = 25, prefetchDistance = 50, enablePlaceholders = false)

    fun getFeed(subreddit: String = ""): LiveData<PagedList<Link>> {
        if (!::feed.isInitialized) {
            val sourceFactory = SubredditDataSourceFactory(feedRepository, subreddit, SubredditSort.BEST, viewModelScope)
            feed = sourceFactory.toLiveData(config = pagingConfig)
        }
        return feed
    }
}