package com.kainalu.readerforreddit.subscription

import com.kainalu.readerforreddit.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubscriptionRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getDefaultSubreddits() {
        apiService.getDefaultSubreddits(100)
    }
}