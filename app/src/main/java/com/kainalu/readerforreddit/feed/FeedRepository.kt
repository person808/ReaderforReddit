package com.kainalu.readerforreddit.feed

import android.util.Log
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.paging.Config
import androidx.paging.PagedList
import com.kainalu.readerforreddit.network.ApiService
import com.kainalu.readerforreddit.network.Resource
import com.kainalu.readerforreddit.network.models.Link
import com.kainalu.readerforreddit.network.models.Listing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FeedRepository @Inject constructor(private val apiService: ApiService) {

    private val pagingConfig = Config(pageSize = 25, prefetchDistance = 50, enablePlaceholders = false)

    suspend fun getFeed(
        subreddit: String = "",
        sort: SubredditSort,
        sortDuration: Duration,
        after: String = ""
    ): Resource<Listing<Link>> {
        if (subreddit.isNotEmpty() && sort == SubredditSort.BEST) {
            throw IllegalArgumentException("Subreddit sort ${sort.name} is invalid for $subreddit")
        }

        return try {
            val response = if (subreddit.isNotEmpty()) {
                apiService.getSubreddit(subreddit, sort.name.toLowerCase(), sortDuration.string, after)
            } else {
                apiService.getSubreddit(sort.name.toLowerCase(), sortDuration.string, after)
            }

            if (response.isSuccessful) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.body(), null)
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "Unknown error", e)
            Resource.Error(error = e)
        }
    }

    suspend fun getPagedList(subreddit: String, sort: SubredditSort, sortDuration: Duration): PagedList<Link> =
        coroutineScope {
            val sourceFactory = SubredditDataSourceFactory(
                this@FeedRepository,
                subreddit,
                sort,
                sortDuration,
                this
            )
            withContext(Dispatchers.IO) {
                PagedList.Builder(sourceFactory.create(), pagingConfig)
                    .setFetchExecutor(ArchTaskExecutor.getIOThreadExecutor())
                    .setNotifyExecutor(ArchTaskExecutor.getMainThreadExecutor())
                    .build()
            }
        }

    fun getDefaultSort(subreddit: String): SubredditSort {
        return when (subreddit.toLowerCase()) {
            "" -> SubredditSort.BEST
            else -> SubredditSort.HOT
        }
    }

    fun getAvailableSorts(subreddit: String): List<SubredditSort> =
        if (subreddit == "") {
            SubredditSort.values().toList()
        } else {
            SubredditSort.values().toList().drop(1)
        }

    companion object {
        private const val TAG = "FeedRepository"
    }
}