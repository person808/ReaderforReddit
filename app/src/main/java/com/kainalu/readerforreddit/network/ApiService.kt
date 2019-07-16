package com.kainalu.readerforreddit.network

import com.kainalu.readerforreddit.network.models.Comment
import com.kainalu.readerforreddit.network.models.Enveloped
import com.kainalu.readerforreddit.network.models.Link
import com.kainalu.readerforreddit.network.models.Listing
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @Enveloped
    @GET("/{sort}")
    suspend fun getSubreddit(
        @Path("sort") sort: String,
        @Query("after") after: String = ""
    ): Listing<Link>

    @Enveloped
    @GET("/r/{name}/{sort}")
    suspend fun getSubreddit(
        @Path("name") subreddit: String,
        @Path("sort") sort: String,
        @Query("after") after: String = ""
    ): Listing<Link>

    @Enveloped
    @GET("/r/{name}/comments/{id}")
    suspend fun getComments(@Path("name") subreddit: String, @Path("id") threadId: String): List<Listing<Comment>>
}