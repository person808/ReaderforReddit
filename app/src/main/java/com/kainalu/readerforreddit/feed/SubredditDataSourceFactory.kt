package com.kainalu.readerforreddit.feed

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.kainalu.readerforreddit.network.models.Link
import kotlinx.coroutines.CoroutineScope

class SubredditDataSourceFactory(
    private val repository: FeedRepository,
    private val subreddit: String,
    private val sort: SubredditSort,
    private val sortDuration: Duration,
    private val coroutineScope: CoroutineScope
) : DataSource.Factory<String, Link>() {

    val sourceLiveData = MutableLiveData<SubredditDataSource>()

    override fun create(): DataSource<String, Link> {
        return SubredditDataSource(repository, subreddit, sort, sortDuration, coroutineScope).also {
            sourceLiveData.postValue(it)
        }
    }
}