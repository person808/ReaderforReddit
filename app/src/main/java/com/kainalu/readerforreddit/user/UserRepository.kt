package com.kainalu.readerforreddit.user

import com.kainalu.readerforreddit.network.ApiService
import com.kainalu.readerforreddit.network.TokenManager
import com.kainalu.readerforreddit.network.models.Subreddit
import com.kainalu.readerforreddit.util.collectListing
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {
    suspend fun getUser(username: String): User {
        val response = apiService.getUser(username)
        return if (response.isSuccessful) {
            User(response.body() ?: throw HttpException(response))
        } else {
            throw HttpException(response)
        }
    }

    suspend fun getUserSubscriptions(): List<Subreddit> {
        return getDefaultSubscriptions()
    }

    suspend fun getDefaultSubscriptions(): List<Subreddit> {
        return collectListing {
            apiService.getDefaultSubreddits(limit = 100, after = it)
        }
    }
}