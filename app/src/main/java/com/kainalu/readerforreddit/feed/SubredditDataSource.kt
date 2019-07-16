package com.kainalu.readerforreddit.feed

import androidx.paging.PageKeyedDataSource
import com.kainalu.readerforreddit.network.models.Link
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SubredditDataSource(
    private val repository: FeedRepository,
    private val subreddit: String,
    private val coroutineScope: CoroutineScope
) : PageKeyedDataSource<String, Link>() {

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Link>) {
        // ignored because we only need to append to our initial load
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Link>) {
        coroutineScope.launch {
            val request = repository.getFeed(subreddit, SubredditSort.BEST, params.key)
            callback.onResult(request.children, request.after)
        }
    }

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, Link>) {
        coroutineScope.launch {
            val request = repository.getFeed(subreddit, SubredditSort.BEST)
            callback.onResult(request.children, request.before, request.after)
        }
    }
}