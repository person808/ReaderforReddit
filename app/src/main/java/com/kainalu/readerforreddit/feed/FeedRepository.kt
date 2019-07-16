package com.kainalu.readerforreddit.feed

import com.kainalu.readerforreddit.network.ApiService
import com.kainalu.readerforreddit.network.Resource
import com.kainalu.readerforreddit.network.models.Link
import com.kainalu.readerforreddit.network.models.Listing
import javax.inject.Inject

class FeedRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getFeed(subreddit: String = "", sort: SubredditSort, after: String = ""): Resource<Listing<Link>> {
        if (subreddit.isNotEmpty() && sort == SubredditSort.BEST) {
            throw IllegalArgumentException("Subreddit sort ${sort.name} is invalid for $subreddit")
        }

        return try {
            val response = if (subreddit.isNotEmpty()) {
                apiService.getSubreddit(subreddit, sort.name.toLowerCase(), after)
            } else {
                apiService.getSubreddit(sort.name.toLowerCase(), after)
            }

            if (response.isSuccessful) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.body(), null)
            }
        } catch (e: Exception) {
            Resource.Error(error = e)
        }
    }
}