package com.kainalu.readerforreddit.feed

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.kainalu.readerforreddit.network.ApiService
import com.kainalu.readerforreddit.network.models.Link

class SubredditDataSourceFactory(
    private val apiService: ApiService,
    private val subreddit: String
) : DataSource.Factory<String, Link>() {

    val sourceLiveData = MutableLiveData<SubredditDataSource>()

    override fun create(): DataSource<String, Link> {
        return SubredditDataSource(apiService, subreddit).also {
            sourceLiveData.postValue(it)
        }
    }
}