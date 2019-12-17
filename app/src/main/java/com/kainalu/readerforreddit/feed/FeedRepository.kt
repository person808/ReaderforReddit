package com.kainalu.readerforreddit.feed

import android.util.Log
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.paging.Config
import androidx.paging.PagedList
import com.kainalu.readerforreddit.models.LinkData
import com.kainalu.readerforreddit.network.ApiService
import com.kainalu.readerforreddit.network.Resource
import com.kainalu.readerforreddit.network.models.Link
import com.kainalu.readerforreddit.network.models.Listing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedRepository @Inject constructor(private val apiService: ApiService) {

    private val pagingConfig = Config(pageSize = 25, prefetchDistance = 50, enablePlaceholders = false)

    suspend fun getFeed(
        subredditName: String = "",
        sort: SubredditSort,
        sortDuration: Duration,
        after: String = ""
    ): Resource<Listing<Link>> {
        if (subredditName.isNotEmpty() && sort == SubredditSort.BEST) {
            throw IllegalArgumentException("Subreddit subreddit_sort ${sort.name} is invalid for $subredditName")
        }

        return try {
            val subreddit = if (subredditName.isNotEmpty()) {
                apiService.getSubreddit(
                    subredditName,
                    sort.name.toLowerCase(),
                    sortDuration.string,
                    after
                )
            } else {
                apiService.getSubreddit(sort.name.toLowerCase(), sortDuration.string, after)
            }
            Resource.Success(subreddit)
        } catch (e: HttpException) {
            Log.e(TAG, e.message, e)
            Resource.Error(error = e)
        }
    }

    suspend fun getPagedList(
        subreddit: String,
        sort: SubredditSort,
        sortDuration: Duration,
        scope: CoroutineScope
    ): PagedList<LinkData> =
        withContext(Dispatchers.IO) {
            val sourceFactory = SubredditDataSourceFactory(
                this@FeedRepository,
                subreddit,
                sort,
                sortDuration,
                scope
            )
            PagedList.Builder(sourceFactory.create(), pagingConfig)
                .setFetchExecutor(ArchTaskExecutor.getIOThreadExecutor())
                .setNotifyExecutor(ArchTaskExecutor.getMainThreadExecutor())
                .build()
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