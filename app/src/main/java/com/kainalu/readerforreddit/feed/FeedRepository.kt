package com.kainalu.readerforreddit.feed

import androidx.lifecycle.LiveData
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.kainalu.readerforreddit.network.ApiService
import com.kainalu.readerforreddit.network.models.Link
import javax.inject.Inject

class FeedRepository @Inject constructor(private val apiService: ApiService) {

    private val pagingConfig = Config(pageSize = 25, prefetchDistance = 50, enablePlaceholders = false)

    fun getFeed(subreddit: String = "", sort: SubredditSort): LiveData<PagedList<Link>> {
        if (subreddit.isNotEmpty() && sort == SubredditSort.BEST) {
            throw IllegalArgumentException("Subreddit sort ${sort.name} is invalid for $subreddit")
        }

        val sourceFactory = SubredditDataSourceFactory(apiService, subreddit)
        val livePagedList = sourceFactory.toLiveData(config = pagingConfig)
        return livePagedList
    }
}