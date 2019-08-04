package com.kainalu.readerforreddit.feed

import androidx.paging.PageKeyedDataSource
import com.kainalu.readerforreddit.models.LinkData
import com.kainalu.readerforreddit.models.LinkDataImpl
import com.kainalu.readerforreddit.network.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SubredditDataSource(
    private val repository: FeedRepository,
    private val subreddit: String,
    private var sort: SubredditSort,
    private var sortDuration: Duration,
    private val coroutineScope: CoroutineScope
) : PageKeyedDataSource<String, LinkData>() {

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, LinkData>) {
        // ignored because we only need to append to our initial load
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, LinkData>) {
        coroutineScope.launch {
            when (val resource = repository.getFeed(subreddit, sort, sortDuration, params.key)) {
                is Resource.Success -> callback.onResult(
                    resource.data.children.map { LinkDataImpl(it) },
                    resource.data.after
                )
            }
        }
    }

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, LinkData>) {
        coroutineScope.launch {
            when (val resource = repository.getFeed(subreddit, sort, sortDuration)) {
                is Resource.Success -> callback.onResult(
                    resource.data.children.map { LinkDataImpl(it) },
                    resource.data.before,
                    resource.data.after
                )
            }
        }
    }
}