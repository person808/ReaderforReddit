package com.kainalu.readerforreddit.feed

import android.util.Log
import com.kainalu.readerforreddit.network.ApiService
import com.kainalu.readerforreddit.network.Resource
import com.kainalu.readerforreddit.network.models.Link
import com.kainalu.readerforreddit.network.models.Listing
import javax.inject.Inject

class FeedRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getFeed(subreddit: String = "", sort: SubredditSort, sortDuration: Duration, after: String = ""): Resource<Listing<Link>> {
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