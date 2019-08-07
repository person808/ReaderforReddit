package com.kainalu.readerforreddit.user

import com.kainalu.readerforreddit.network.models.Subreddit

interface UserData {

    val id: String?
    val name: String?
    val username: String?
    val commentKarma: Int?
    val linkKarma: Int?
    val totalKarma: Int?

    suspend fun getSubscriptions(): List<Subreddit>
}