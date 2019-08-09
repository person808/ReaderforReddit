package com.kainalu.readerforreddit.models

import android.os.Parcelable
import com.kainalu.readerforreddit.network.models.Subreddit

interface UserData : Parcelable {

    val id: String
    val name: String?
    val username: String?
    val commentKarma: Int?
    val linkKarma: Int?
    val totalKarma: Int?

    suspend fun getSubscriptions(): List<Subreddit>
}