package com.kainalu.readerforreddit.feed

import androidx.paging.PageKeyedDataSource
import com.kainalu.readerforreddit.network.ApiService
import com.kainalu.readerforreddit.network.models.Link
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SubredditDataSource(
    private val apiService: ApiService,
    private val subreddit: String
) : PageKeyedDataSource<String, Link>() {

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Link>) {
        // ignored because we only need to append to our initial load
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Link>) {
        GlobalScope.launch {
            val request = withContext(Dispatchers.IO) {
                apiService.getSubreddit(sort = "best", after = params.key)
            }
            withContext(Dispatchers.Main) {
                callback.onResult(request.children, request.after)
            }
        }
    }

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, Link>) {
        GlobalScope.launch {
            val request = withContext(Dispatchers.IO) {
                apiService.getSubreddit(sort = "best")
            }
            withContext(Dispatchers.Main) {
                callback.onResult(request.children, request.before, request.after)
            }
        }
    }
}